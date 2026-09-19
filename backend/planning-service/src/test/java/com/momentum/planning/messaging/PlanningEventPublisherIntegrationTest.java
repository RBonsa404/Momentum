package com.momentum.planning.messaging;

import com.momentum.common.event.DomainEvent;
import com.momentum.common.event.EventTypes;
import com.momentum.planning.domain.Day;
import com.momentum.planning.domain.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlanningEventPublisherIntegrationTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PlanningEventPublisher publisher;

    @Test
    void taskCreatedPublishesEvent() {
        UUID userId = UUID.randomUUID();
        String correlationId = UUID.randomUUID().toString();
        
        Day day = new Day();
        day.setId(UUID.randomUUID());
        day.setUserId(userId);
        day.setDate(LocalDate.now());
        day.setClosed(false);
        
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDayId(day.getId());
        task.setTitle("Test task");
        task.setStatus(com.momentum.planning.domain.TaskStatus.TODO);
        
        publisher.taskCreated(userId, correlationId, task, day);
        
        ArgumentCaptor<DomainEvent<?>> captor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(rabbitTemplate).convertAndSend(eq(EventTypes.EXCHANGE), eq(EventTypes.TASK_CREATED), captor.capture());
        
        DomainEvent<?> event = captor.getValue();
        assertThat(event.getEventType()).isEqualTo(EventTypes.TASK_CREATED);
        assertThat(event.getCorrelationId()).isEqualTo(correlationId);
        assertThat(event.getUserId()).isEqualTo(userId);
    }

    @Test
    void taskCompletedPublishesEvent() {
        UUID userId = UUID.randomUUID();
        String correlationId = UUID.randomUUID().toString();
        
        Day day = new Day();
        day.setId(UUID.randomUUID());
        day.setUserId(userId);
        day.setDate(LocalDate.now());
        day.setClosed(false);
        
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDayId(day.getId());
        task.setTitle("Test task");
        task.setStatus(com.momentum.planning.domain.TaskStatus.DONE);
        
        publisher.taskCompleted(userId, correlationId, task, day);
        
        ArgumentCaptor<DomainEvent<?>> captor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(rabbitTemplate).convertAndSend(eq(EventTypes.EXCHANGE), eq(EventTypes.TASK_COMPLETED), captor.capture());
        
        DomainEvent<?> event = captor.getValue();
        assertThat(event.getEventType()).isEqualTo(EventTypes.TASK_COMPLETED);
        assertThat(event.getCorrelationId()).isEqualTo(correlationId);
        assertThat(event.getUserId()).isEqualTo(userId);
    }

    @Test
    void taskPostponedPublishesEvent() {
        UUID userId = UUID.randomUUID();
        String correlationId = UUID.randomUUID().toString();
        
        Day day = new Day();
        day.setId(UUID.randomUUID());
        day.setUserId(userId);
        day.setDate(LocalDate.now());
        day.setClosed(false);
        
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDayId(day.getId());
        task.setTitle("Test task");
        task.setStatus(com.momentum.planning.domain.TaskStatus.TODO);
        task.setPostponedCount(1);
        
        publisher.taskPostponed(userId, correlationId, task, day);
        
        ArgumentCaptor<DomainEvent<?>> captor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(rabbitTemplate).convertAndSend(eq(EventTypes.EXCHANGE), eq(EventTypes.TASK_POSTPONED), captor.capture());
        
        DomainEvent<?> event = captor.getValue();
        assertThat(event.getEventType()).isEqualTo(EventTypes.TASK_POSTPONED);
        assertThat(event.getCorrelationId()).isEqualTo(correlationId);
        assertThat(event.getUserId()).isEqualTo(userId);
    }

    @Test
    void dayClosedPublishesEvent() {
        UUID userId = UUID.randomUUID();
        String correlationId = UUID.randomUUID().toString();
        
        Day day = new Day();
        day.setId(UUID.randomUUID());
        day.setUserId(userId);
        day.setDate(LocalDate.now());
        day.setClosed(true);
        
        publisher.dayClosed(userId, correlationId, day);
        
        ArgumentCaptor<DomainEvent<?>> captor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(rabbitTemplate).convertAndSend(eq(EventTypes.EXCHANGE), eq(EventTypes.DAY_CLOSED), captor.capture());
        
        DomainEvent<?> event = captor.getValue();
        assertThat(event.getEventType()).isEqualTo(EventTypes.DAY_CLOSED);
        assertThat(event.getCorrelationId()).isEqualTo(correlationId);
        assertThat(event.getUserId()).isEqualTo(userId);
    }
}