package com.momentum.server.service;

import com.momentum.server.domain.goal.Goal;
import com.momentum.server.domain.goal.Habit;
import com.momentum.server.domain.goal.Milestone;
import com.momentum.server.dto.goal.CreateGoalRequest;
import com.momentum.server.dto.goal.CreateHabitRequest;
import com.momentum.server.dto.goal.CreateMilestoneRequest;
import com.momentum.server.dto.goal.GoalDto;
import com.momentum.server.dto.goal.HabitDto;
import com.momentum.server.dto.goal.MilestoneDto;
import com.momentum.server.repository.GoalRepository;
import com.momentum.server.repository.HabitRepository;
import com.momentum.server.repository.MilestoneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class GoalService {
    private final GoalRepository goals;
    private final MilestoneRepository milestones;
    private final HabitRepository habits;

    public GoalService(GoalRepository goals, MilestoneRepository milestones, HabitRepository habits) {
        this.goals = goals;
        this.milestones = milestones;
        this.habits = habits;
    }

    public List<Goal> list(UUID userId) {
        return goals.findByUserId(userId);
    }

    @Transactional
    public Goal create(UUID userId, CreateGoalRequest request) {
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
        return goals.save(goal);
    }

    public List<Milestone> milestones(UUID goalId) {
        return milestones.findByGoalId(goalId);
    }

    @Transactional
    public Milestone addMilestone(UUID userId, UUID goalId, CreateMilestoneRequest request) {
        Milestone milestone = new Milestone();
        milestone.setGoalId(goalId);
        milestone.setTitle(request.getTitle());
        milestone.setDueDate(request.getDueDate());
        milestone.setDone(false);
        Milestone saved = milestones.save(milestone);
        recalcProgress(userId, goalId);
        return saved;
    }

    @Transactional
    public Milestone toggleMilestone(UUID userId, UUID milestoneId, boolean done) {
        Milestone milestone = milestones.findById(milestoneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Milestone not found"));
        milestone.setDone(done);
        milestones.save(milestone);
        recalcProgress(userId, milestone.getGoalId());
        return milestone;
    }

    public List<Habit> habits(UUID userId) {
        return habits.findByUserId(userId);
    }

    @Transactional
    public Habit createHabit(UUID userId, CreateHabitRequest request) {
        Habit habit = new Habit();
        habit.setUserId(userId);
        habit.setTitle(request.getTitle());
        habit.setCurrentStreak(0);
        return habits.save(habit);
    }

    @Transactional
    public Habit tickHabit(UUID habitId) {
        Habit habit = habits.findById(habitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habit not found"));
        habit.setCurrentStreak(habit.getCurrentStreak() + 1);
        return habits.save(habit);
    }

    private void recalcProgress(UUID userId, UUID goalId) {
        List<Milestone> all = milestones.findByGoalId(goalId);
        Goal goal = goals.findById(goalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));
        if (all.isEmpty()) {
            return;
        }
        long done = all.stream().filter(Milestone::isDone).count();
        goal.setProgressPercent((int) Math.round(100.0 * done / all.size()));
        goals.save(goal);
    }

    public GoalDto toDto(Goal goal) {
        GoalDto dto = new GoalDto();
        dto.setId(goal.getId());
        dto.setUserId(goal.getUserId());
        dto.setTitle(goal.getTitle());
        dto.setSpecificText(goal.getSpecificText());
        dto.setMeasurableText(goal.getMeasurableText());
        dto.setAchievableText(goal.getAchievableText());
        dto.setRelevantText(goal.getRelevantText());
        dto.setDueDate(goal.getDueDate());
        dto.setProgressPercent(goal.getProgressPercent());
        dto.setLinkedTaskOriginId(goal.getLinkedTaskOriginId());
        return dto;
    }

    public MilestoneDto toDto(Milestone milestone) {
        MilestoneDto dto = new MilestoneDto();
        dto.setId(milestone.getId());
        dto.setGoalId(milestone.getGoalId());
        dto.setTitle(milestone.getTitle());
        dto.setDueDate(milestone.getDueDate());
        dto.setDone(milestone.isDone());
        return dto;
    }

    public HabitDto toDto(Habit habit) {
        HabitDto dto = new HabitDto();
        dto.setId(habit.getId());
        dto.setUserId(habit.getUserId());
        dto.setTitle(habit.getTitle());
        dto.setCurrentStreak(habit.getCurrentStreak());
        return dto;
    }
}
