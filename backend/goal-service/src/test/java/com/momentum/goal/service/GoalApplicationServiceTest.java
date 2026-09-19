package com.momentum.goal.service;

import com.momentum.goal.domain.Goal;
import com.momentum.goal.domain.Habit;
import com.momentum.goal.domain.Milestone;
import com.momentum.goal.dto.CreateGoalRequest;
import com.momentum.goal.dto.CreateHabitRequest;
import com.momentum.goal.dto.CreateMilestoneRequest;
import com.momentum.goal.messaging.GoalEventPublisher;
import com.momentum.goal.repository.GoalRepository;
import com.momentum.goal.repository.HabitRepository;
import com.momentum.goal.repository.MilestoneRepository;
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
class GoalApplicationServiceTest {

    @Mock
    private GoalRepository goals;

    @Mock
    private MilestoneRepository milestones;

    @Mock
    private HabitRepository habits;

    @Mock
    private GoalEventPublisher publisher;

    @InjectMocks
    private GoalApplicationService service;

    @Test
    void create_savesGoalAndPublishesEvent() {
        UUID userId = UUID.randomUUID();
        CreateGoalRequest request = new CreateGoalRequest();
        request.setTitle("Test Goal");
        request.setSpecificText("Specific");
        request.setMeasurableText("Measurable");
        request.setAchievableText("Achievable");
        request.setRelevantText("Relevant");
        request.setDueDate(LocalDate.now().plusDays(30));

        when(goals.save(any(Goal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Goal result = service.create(userId, "correlation-123", request);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getTitle()).isEqualTo("Test Goal");
        assertThat(result.getProgressPercent()).isEqualTo(0);
        verify(publisher).goalProgressUpdated(eq(userId), eq("correlation-123"), any(Goal.class));
    }

    @Test
    void addMilestone_recalculatesProgress() {
        UUID userId = UUID.randomUUID();
        UUID goalId = UUID.randomUUID();
        CreateMilestoneRequest request = new CreateMilestoneRequest();
        request.setTitle("Milestone 1");

        Goal goal = new Goal();
        goal.setId(goalId);
        goal.setUserId(userId);
        goal.setProgressPercent(0);

        Milestone existingMilestone = new Milestone();
        existingMilestone.setId(UUID.randomUUID());
        existingMilestone.setGoalId(goalId);
        existingMilestone.setDone(true);

        when(milestones.save(any(Milestone.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(milestones.findByGoalId(goalId)).thenReturn(List.of(existingMilestone));
        when(goals.findById(goalId)).thenReturn(Optional.of(goal));
        when(goals.save(any(Goal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Milestone result = service.addMilestone(userId, "correlation-456", goalId, request);

        assertThat(result.getTitle()).isEqualTo("Milestone 1");
        assertThat(result.isDone()).isFalse();
        verify(publisher).goalProgressUpdated(eq(userId), eq("correlation-456"), any(Goal.class));
    }

    @Test
    void toggleMilestone_updatesProgress() {
        UUID userId = UUID.randomUUID();
        UUID goalId = UUID.randomUUID();
        UUID milestoneId = UUID.randomUUID();

        Milestone milestone = new Milestone();
        milestone.setId(milestoneId);
        milestone.setGoalId(goalId);
        milestone.setDone(false);

        Goal goal = new Goal();
        goal.setId(goalId);
        goal.setUserId(userId);
        goal.setProgressPercent(0);

        when(milestones.findById(milestoneId)).thenReturn(Optional.of(milestone));
        when(milestones.findByGoalId(goalId)).thenReturn(List.of(milestone));
        when(goals.findById(goalId)).thenReturn(Optional.of(goal));
        when(goals.save(any(Goal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Milestone result = service.toggleMilestone(userId, "correlation-789", milestoneId, true);

        assertThat(result.isDone()).isTrue();
        verify(publisher).goalProgressUpdated(eq(userId), eq("correlation-789"), any(Goal.class));
    }

    @Test
    void createHabit_savesHabit() {
        UUID userId = UUID.randomUUID();
        CreateHabitRequest request = new CreateHabitRequest();
        request.setTitle("Exercise");

        when(habits.save(any(Habit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Habit result = service.createHabit(userId, request);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getTitle()).isEqualTo("Exercise");
        assertThat(result.getCurrentStreak()).isEqualTo(0);
    }

    @Test
    void tickHabit_incrementsStreak() {
        UUID habitId = UUID.randomUUID();
        Habit habit = new Habit();
        habit.setId(habitId);
        habit.setCurrentStreak(5);

        when(habits.findById(habitId)).thenReturn(Optional.of(habit));
        when(habits.save(any(Habit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Habit result = service.tickHabit(habitId);

        assertThat(result.getCurrentStreak()).isEqualTo(6);
    }
}