package com.momentum.goal.controller;

import com.momentum.common.tracing.CorrelationIds;
import com.momentum.goal.domain.Goal;
import com.momentum.goal.domain.Habit;
import com.momentum.goal.domain.Milestone;
import com.momentum.goal.repository.GoalRepository;
import com.momentum.goal.repository.HabitRepository;
import com.momentum.goal.repository.MilestoneRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
public class GoalController {
    private final GoalRepository goals;
    private final MilestoneRepository milestones;
    private final HabitRepository habits;

    public GoalController(GoalRepository goals, MilestoneRepository milestones, HabitRepository habits) {
        this.goals = goals;
        this.milestones = milestones;
        this.habits = habits;
    }

    @GetMapping
    public List<Goal> list(HttpServletRequest request) {
        return goals.findByUserId(userId(request));
    }

    @PostMapping
    public Goal create(HttpServletRequest request, @RequestBody Map<String, String> body) {
        Goal goal = new Goal();
        goal.setUserId(userId(request));
        goal.setTitle(body.get("title"));
        goal.setSpecificText(body.get("specificText"));
        goal.setMeasurableText(body.get("measurableText"));
        goal.setAchievableText(body.get("achievableText"));
        goal.setRelevantText(body.get("relevantText"));
        if (body.get("dueDate") != null) {
            goal.setDueDate(LocalDate.parse(body.get("dueDate")));
        }
        if (body.get("linkedTaskOriginId") != null) {
            goal.setLinkedTaskOriginId(UUID.fromString(body.get("linkedTaskOriginId")));
        }
        return goals.save(goal);
    }

    @GetMapping("/{id}/milestones")
    public List<Milestone> milestones(@PathVariable UUID id) {
        return milestones.findByGoalId(id);
    }

    @PostMapping("/{id}/milestones")
    public Milestone addMilestone(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        Milestone milestone = new Milestone();
        milestone.setGoalId(id);
        milestone.setTitle(body.get("title"));
        if (body.get("dueDate") != null) {
            milestone.setDueDate(LocalDate.parse(body.get("dueDate")));
        }
        Milestone saved = milestones.save(milestone);
        recalc(id);
        return saved;
    }

    @PatchMapping("/milestones/{id}")
    public Milestone toggle(@PathVariable UUID id, @RequestBody Map<String, Boolean> body) {
        Milestone milestone = milestones.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        milestone.setDone(Boolean.TRUE.equals(body.get("done")));
        milestones.save(milestone);
        recalc(milestone.getGoalId());
        return milestone;
    }

    @GetMapping("/habits")
    public List<Habit> habits(HttpServletRequest request) {
        return habits.findByUserId(userId(request));
    }

    @PostMapping("/habits")
    public Habit createHabit(HttpServletRequest request, @RequestBody Map<String, String> body) {
        Habit habit = new Habit();
        habit.setUserId(userId(request));
        habit.setTitle(body.get("title"));
        return habits.save(habit);
    }

    @PostMapping("/habits/{id}/tick")
    public Habit tick(@PathVariable UUID id) {
        Habit habit = habits.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        habit.setCurrentStreak(habit.getCurrentStreak() + 1);
        return habits.save(habit);
    }

    private void recalc(UUID goalId) {
        List<Milestone> all = milestones.findByGoalId(goalId);
        Goal goal = goals.findById(goalId).orElseThrow();
        if (all.isEmpty()) {
            return;
        }
        long done = all.stream().filter(Milestone::isDone).count();
        goal.setProgressPercent((int) Math.round(100.0 * done / all.size()));
        goals.save(goal);
    }

    private UUID userId(HttpServletRequest request) {
        String header = request.getHeader(CorrelationIds.USER_HEADER);
        if (header == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return UUID.fromString(header);
    }
}
