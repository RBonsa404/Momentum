package com.momentum.server.controller;

import com.momentum.server.config.SecurityUtils;
import com.momentum.server.dto.goal.CreateGoalRequest;
import com.momentum.server.dto.goal.CreateHabitRequest;
import com.momentum.server.dto.goal.CreateMilestoneRequest;
import com.momentum.server.dto.goal.GoalDto;
import com.momentum.server.dto.goal.HabitDto;
import com.momentum.server.dto.goal.MilestoneDto;
import com.momentum.server.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
public class GoalController {
    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public List<GoalDto> listGoals() {
        return goalService.list(SecurityUtils.getUserId()).stream().map(goalService::toDto).toList();
    }

    @PostMapping
    public GoalDto createGoal(@Valid @RequestBody CreateGoalRequest request) {
        return goalService.toDto(goalService.create(SecurityUtils.getUserId(), request));
    }

    @GetMapping("/{goalId}/milestones")
    public List<MilestoneDto> milestones(@PathVariable UUID goalId) {
        return goalService.milestones(goalId).stream().map(goalService::toDto).toList();
    }

    @PostMapping("/{goalId}/milestones")
    public MilestoneDto addMilestone(@PathVariable UUID goalId, @Valid @RequestBody CreateMilestoneRequest request) {
        return goalService.toDto(goalService.addMilestone(SecurityUtils.getUserId(), goalId, request));
    }

    @PatchMapping("/milestones/{id}")
    public MilestoneDto toggleMilestone(@PathVariable UUID id, @RequestBody Map<String, Boolean> body) {
        return goalService.toDto(goalService.toggleMilestone(SecurityUtils.getUserId(), id, Boolean.TRUE.equals(body.get("done"))));
    }

    @GetMapping("/habits")
    public List<HabitDto> habits() {
        return goalService.habits(SecurityUtils.getUserId()).stream().map(goalService::toDto).toList();
    }

    @PostMapping("/habits")
    public HabitDto createHabit(@Valid @RequestBody CreateHabitRequest request) {
        return goalService.toDto(goalService.createHabit(SecurityUtils.getUserId(), request));
    }

    @PostMapping("/habits/{id}/tick")
    public HabitDto tickHabit(@PathVariable UUID id) {
        return goalService.toDto(goalService.tickHabit(id));
    }
}
