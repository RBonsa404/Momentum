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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class GoalApplicationService {
    private final GoalRepository goals;
    private final MilestoneRepository milestones;
    private final HabitRepository habits;
    private final GoalEventPublisher publisher;

    public GoalApplicationService(GoalRepository goals, MilestoneRepository milestones, HabitRepository habits, GoalEventPublisher publisher) {
        this.goals = goals;
        this.milestones = milestones;
        this.habits = habits;
        this.publisher = publisher;
    }

    public List<Goal> list(UUID userId) {
        return goals.findByUserId(userId);
    }

    public Goal create(UUID userId, String correlationId, CreateGoalRequest request) {
        Goal goal = new Goal();
        goal.setUserId(userId);
        goal.setTitle(request.getTitle());
        goal.setSpecificText(request.getSpecificText());
        goal.setMeasurableText(request.getMeasurableText());
        goal.setAchievableText(request.getAchievableText());
        goal.setRelevantText(request.getRelevantText());
        goal.setDueDate(request.getDueDate());
        goal.setLinkedTaskOriginId(request.getLinkedTaskOriginId());
        goal.setProgressPercent(0);
        Goal saved = goals.save(goal);
        publisher.goalProgressUpdated(userId, correlationId, saved);
        return saved;
    }

    public List<Milestone> milestones(UUID goalId) {
        return milestones.findByGoalId(goalId);
    }

    public Milestone addMilestone(UUID userId, String correlationId, UUID goalId, CreateMilestoneRequest request) {
        Milestone milestone = new Milestone();
        milestone.setGoalId(goalId);
        milestone.setTitle(request.getTitle());
        milestone.setDueDate(request.getDueDate());
        milestone.setDone(false);
        Milestone saved = milestones.save(milestone);
        recalcProgress(userId, correlationId, goalId);
        return saved;
    }

    public Milestone toggleMilestone(UUID userId, String correlationId, UUID milestoneId, boolean done) {
        Milestone milestone = milestones.findById(milestoneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Milestone not found"));
        milestone.setDone(done);
        milestones.save(milestone);
        recalcProgress(userId, correlationId, milestone.getGoalId());
        return milestone;
    }

    public List<Habit> habits(UUID userId) {
        return habits.findByUserId(userId);
    }

    public Habit createHabit(UUID userId, CreateHabitRequest request) {
        Habit habit = new Habit();
        habit.setUserId(userId);
        habit.setTitle(request.getTitle());
        habit.setCurrentStreak(0);
        return habits.save(habit);
    }

    public Habit tickHabit(UUID habitId) {
        Habit habit = habits.findById(habitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habit not found"));
        habit.setCurrentStreak(habit.getCurrentStreak() + 1);
        return habits.save(habit);
    }

    private void recalcProgress(UUID userId, String correlationId, UUID goalId) {
        List<Milestone> all = milestones.findByGoalId(goalId);
        Goal goal = goals.findById(goalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));
        if (all.isEmpty()) {
            return;
        }
        long done = all.stream().filter(Milestone::isDone).count();
        goal.setProgressPercent((int) Math.round(100.0 * done / all.size()));
        goals.save(goal);
        publisher.goalProgressUpdated(userId, correlationId, goal);
    }
}