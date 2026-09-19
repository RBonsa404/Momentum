package com.momentum.planning.service;

import com.momentum.planning.domain.Day;
import com.momentum.planning.domain.Task;
import com.momentum.planning.domain.TaskStatus;
import com.momentum.planning.messaging.PlanningEventPublisher;
import com.momentum.planning.repository.DayRepository;
import com.momentum.planning.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DayCloseSchedulerTest {

    @Mock
    private DayRepository dayRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CarryOverService carryOverService;

    @Mock
    private PlanningEventPublisher publisher;

    @InjectMocks
    private DayCloseScheduler scheduler;

    @Test
    void closeDaysForAllUsers_processesOpenDays() {
        UUID userId = UUID.randomUUID();
        LocalDate today = LocalDate.now();
        
        Day openDay = new Day();
        openDay.setId(UUID.randomUUID());
        openDay.setUserId(userId);
        openDay.setDate(today);
        openDay.setClosed(false);
        
        when(dayRepository.findByDateAndClosedFalse(today)).thenReturn(List.of(openDay));
        when(taskRepository.findByDayIdAndStatus(openDay.getId(), TaskStatus.TODO)).thenReturn(List.of());
        when(dayRepository.findByUserIdAndDate(userId, today.plusDays(1))).thenReturn(Optional.empty());
        when(dayRepository.save(any(Day.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        scheduler.closeDaysForAllUsers();
        
        verify(dayRepository).findByDateAndClosedFalse(today);
        verify(dayRepository).save(openDay);
        assertThat(openDay.isClosed()).isTrue();
    }

    @Test
    void closeDaysForAllUsers_skipsAlreadyClosedDays() {
        UUID userId = UUID.randomUUID();
        LocalDate today = LocalDate.now();
        
        Day closedDay = new Day();
        closedDay.setId(UUID.randomUUID());
        closedDay.setUserId(userId);
        closedDay.setDate(today);
        closedDay.setClosed(true);
        
        when(dayRepository.findByDateAndClosedFalse(today)).thenReturn(List.of(closedDay));
        
        scheduler.closeDaysForAllUsers();
        
        verify(dayRepository, never()).save(any(Day.class));
        verify(publisher, never()).dayClosed(any(), any(), any());
    }

    @Test
    void closeDaysForAllUsers_postponesUnfinishedTasks() {
        UUID userId = UUID.randomUUID();
        LocalDate today = LocalDate.now();
        
        Day openDay = new Day();
        openDay.setId(UUID.randomUUID());
        openDay.setUserId(userId);
        openDay.setDate(today);
        openDay.setClosed(false);
        
        Task unfinishedTask = new Task();
        unfinishedTask.setId(UUID.randomUUID());
        unfinishedTask.setDayId(openDay.getId());
        unfinishedTask.setTitle("Unfinished task");
        unfinishedTask.setStatus(TaskStatus.TODO);
        
        Task postponedTask = new Task();
        postponedTask.setId(UUID.randomUUID());
        postponedTask.setTitle("Unfinished task");
        postponedTask.setStatus(TaskStatus.TODO);
        postponedTask.setPostponedCount(1);
        
        Day nextDay = new Day();
        nextDay.setId(UUID.randomUUID());
        nextDay.setUserId(userId);
        nextDay.setDate(today.plusDays(1));
        nextDay.setClosed(false);
        
        when(dayRepository.findByDateAndClosedFalse(today)).thenReturn(List.of(openDay));
        when(taskRepository.findByDayIdAndStatus(openDay.getId(), TaskStatus.TODO)).thenReturn(List.of(unfinishedTask));
        when(carryOverService.postponeUnfinished(any(), any())).thenReturn(List.of(postponedTask));
        when(dayRepository.findByUserIdAndDate(userId, today.plusDays(1))).thenReturn(Optional.of(nextDay));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(dayRepository.save(any(Day.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        scheduler.closeDaysForAllUsers();
        
        verify(carryOverService).postponeUnfinished(any(), eq(nextDay.getId()));
        verify(taskRepository).save(postponedTask);
        verify(publisher).taskPostponed(eq(userId), any(), eq(postponedTask), eq(nextDay));
    }
}