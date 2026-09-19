package com.momentum.planning.controller;

import com.momentum.planning.domain.RecurrenceType;
import com.momentum.planning.dto.*;
import com.momentum.planning.mapper.PlanningMapper;
import com.momentum.planning.service.PlanningApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private final PlanningApplicationService planning;
    private final PlanningMapper mapper;

    public PlanningController(PlanningApplicationService planning, PlanningMapper mapper) {
        this.planning = planning;
        this.mapper = mapper;
    }

    @GetMapping("/days")
    public Map<String, Object> day(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        UUID userId = PlanningApplicationService.userId(request);
        var day = planning.getOrCreateDay(userId, date);
        var blocks = planning.blocks(day.getId()).stream().map(mapper::toDto).toList();
        var tasks = planning.tasks(day.getId()).stream().map(mapper::toDto).toList();
        return Map.of("day", mapper.toDto(day), "blocks", blocks, "tasks", tasks);
    }

    @PostMapping("/days/{dayId}/blocks")
    public TimeBlockDto createBlock(@PathVariable UUID dayId, @Valid @RequestBody CreateTimeBlockRequest request) {
        var block = planning.createBlock(dayId, request.getTitle(), request.getStartTime(), request.getEndTime());
        return mapper.toDto(block);
    }

    @PostMapping("/days/{dayId}/tasks")
    public TaskDto createTask(HttpServletRequest request, @PathVariable UUID dayId, @Valid @RequestBody CreateTaskRequest body) {
        var task = planning.createTask(
                PlanningApplicationService.userId(request),
                PlanningApplicationService.cid(request),
                dayId,
                body.getTimeBlockId(),
                body.getTitle(),
                body.getNotes(),
                body.getRecurrence() == null ? RecurrenceType.NONE : body.getRecurrence());
        return mapper.toDto(task);
    }

    @PostMapping("/tasks/{taskId}/complete")
    public TaskDto complete(HttpServletRequest request, @PathVariable UUID taskId) {
        var task = planning.completeTask(
                PlanningApplicationService.userId(request),
                PlanningApplicationService.cid(request),
                taskId);
        return mapper.toDto(task);
    }

    @PostMapping("/days/{dayId}/close")
    public DayDto close(HttpServletRequest request, @PathVariable UUID dayId) {
        var day = planning.closeDay(
                PlanningApplicationService.userId(request),
                PlanningApplicationService.cid(request),
                dayId);
        return mapper.toDto(day);
    }

    @GetMapping("/tasks/{taskId}/subtasks")
    public List<SubTaskDto> subTasks(@PathVariable UUID taskId) {
        return planning.subTasks(taskId).stream().map(mapper::toDto).toList();
    }

    @PostMapping("/tasks/{taskId}/subtasks")
    public SubTaskDto addSub(@PathVariable UUID taskId, @Valid @RequestBody CreateSubTaskRequest body) {
        var sub = planning.addSubTask(taskId, body.getTitle(), body.getSortOrder() == null ? 0 : body.getSortOrder());
        return mapper.toDto(sub);
    }

    @PatchMapping("/subtasks/{id}")
    public SubTaskDto toggle(@PathVariable UUID id, @RequestBody Map<String, Boolean> body) {
        var sub = planning.toggleSubTask(id, Boolean.TRUE.equals(body.get("done")));
        return mapper.toDto(sub);
    }
}
