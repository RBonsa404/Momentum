package com.momentum.goal.controller;

import com.momentum.common.tracing.CorrelationIds;
import com.momentum.goal.dto.*;
import com.momentum.goal.domain.Goal;
import com.momentum.goal.domain.Habit;
import com.momentum.goal.domain.Milestone;
import com.momentum.goal.mapper.GoalMapper;
import com.momentum.goal.service.GoalApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
public class GoalController {
    private final GoalApplicationService service;
    private final GoalMapper mapper;

    public GoalController(GoalApplicationService service, GoalMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<GoalDto> list(HttpServletRequest request) {
        return service.list(userId(request)).stream().map(mapper::toDto).toList();
    }

    @PostMapping
    public GoalDto create(HttpServletRequest request, @Valid @RequestBody CreateGoalRequest body) {
        String cid = request.getHeader(CorrelationIds.HTTP_HEADER);
        Goal goal = service.create(userId(request), cid, body);
        return mapper.toDto(goal);
    }

    @GetMapping("/{id}/milestones")
    public List<MilestoneDto> milestones(@PathVariable UUID id) {
        return service.milestones(id).stream().map(mapper::toDto).toList();
    }

    @PostMapping("/{id}/milestones")
    public MilestoneDto addMilestone(HttpServletRequest request, @PathVariable UUID id, @Valid @RequestBody CreateMilestoneRequest body) {
        String cid = request.getHeader(CorrelationIds.HTTP_HEADER);
        Milestone milestone = service.addMilestone(userId(request), cid, id, body);
        return mapper.toDto(milestone);
    }

    @PatchMapping("/milestones/{id}")
    public MilestoneDto toggle(HttpServletRequest request, @PathVariable UUID id, @RequestBody Map<String, Boolean> body) {
        String cid = request.getHeader(CorrelationIds.HTTP_HEADER);
        Milestone milestone = service.toggleMilestone(userId(request), cid, id, Boolean.TRUE.equals(body.get("done")));
        return mapper.toDto(milestone);
    }

    @GetMapping("/habits")
    public List<HabitDto> habits(HttpServletRequest request) {
        return service.habits(userId(request)).stream().map(mapper::toDto).toList();
    }

    @PostMapping("/habits")
    public HabitDto createHabit(HttpServletRequest request, @Valid @RequestBody CreateHabitRequest body) {
        Habit habit = service.createHabit(userId(request), body);
        return mapper.toDto(habit);
    }

    @PostMapping("/habits/{id}/tick")
    public HabitDto tick(@PathVariable UUID id) {
        Habit habit = service.tickHabit(id);
        return mapper.toDto(habit);
    }

    private UUID userId(HttpServletRequest request) {
        String header = request.getHeader(CorrelationIds.USER_HEADER);
        if (header == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED);
        }
        return UUID.fromString(header);
    }
}
