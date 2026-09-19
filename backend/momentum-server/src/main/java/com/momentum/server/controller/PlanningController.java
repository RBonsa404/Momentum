package com.momentum.server.controller;

import com.momentum.server.config.SecurityUtils;
import com.momentum.server.domain.planning.RecurrenceType;
import com.momentum.server.dto.planning.CreateSubTaskRequest;
import com.momentum.server.dto.planning.CreateTaskRequest;
import com.momentum.server.dto.planning.CreateTimeBlockRequest;
import com.momentum.server.dto.planning.DayDto;
import com.momentum.server.dto.planning.SubTaskDto;
import com.momentum.server.dto.planning.TaskDto;
import com.momentum.server.dto.planning.TimeBlockDto;
import com.momentum.server.service.PlanningService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/planning")
public class PlanningController {
    private final PlanningService planning;

    public PlanningController(PlanningService planning) {
        this.planning = planning;
    }

    @GetMapping("/days")
    public Map<String, Object> day(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        UUID userId = SecurityUtils.getUserId();
        var day = planning.getOrCreateDay(userId, date);
        var blocks = planning.blocks(day.getId()).stream().map(planning::toDto).toList();
        var tasks = planning.tasks(day.getId()).stream().map(planning::toDto).toList();
        return Map.of("day", planning.toDto(day), "blocks", blocks, "tasks", tasks);
    }

    @PostMapping("/days/{dayId}/blocks")
    public TimeBlockDto createBlock(@PathVariable UUID dayId, @Valid @RequestBody CreateTimeBlockRequest request) {
        var block = planning.createBlock(dayId, request.getTitle(), request.getStartTime(), request.getEndTime());
        return planning.toDto(block);
    }

    @PostMapping("/days/{dayId}/tasks")
    public TaskDto createTask(@PathVariable UUID dayId, @Valid @RequestBody CreateTaskRequest body) {
        UUID userId = SecurityUtils.getUserId();
        var task = planning.createTask(
                userId,
                dayId,
                body.getTimeBlockId(),
                body.getTitle(),
                body.getNotes(),
                body.getRecurrence() == null ? RecurrenceType.NONE : body.getRecurrence());
        return planning.toDto(task);
    }

    @PostMapping("/tasks/{taskId}/complete")
    public TaskDto complete(@PathVariable UUID taskId) {
        UUID userId = SecurityUtils.getUserId();
        var task = planning.completeTask(userId, taskId);
        return planning.toDto(task);
    }

    @PostMapping("/days/{dayId}/close")
    public DayDto close(@PathVariable UUID dayId) {
        UUID userId = SecurityUtils.getUserId();
        var day = planning.closeDay(userId, dayId);
        return planning.toDto(day);
    }

    @GetMapping("/tasks/{taskId}/subtasks")
    public List<SubTaskDto> subTasks(@PathVariable UUID taskId) {
        return planning.subTasks(taskId).stream().map(planning::toDto).toList();
    }

    @PostMapping("/tasks/{taskId}/subtasks")
    public SubTaskDto addSub(@PathVariable UUID taskId, @Valid @RequestBody CreateSubTaskRequest body) {
        var sub = planning.addSubTask(taskId, body.getTitle(), body.getSortOrder() == null ? 0 : body.getSortOrder());
        return planning.toDto(sub);
    }

    @PatchMapping("/subtasks/{id}")
    public SubTaskDto toggle(@PathVariable UUID id, @RequestBody Map<String, Boolean> body) {
        var sub = planning.toggleSubTask(id, Boolean.TRUE.equals(body.get("done")));
        return planning.toDto(sub);
    }
}
