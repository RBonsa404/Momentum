package com.momentum.planning.controller;

import com.momentum.planning.domain.Day;
import com.momentum.planning.domain.RecurrenceType;
import com.momentum.planning.domain.SubTask;
import com.momentum.planning.domain.Task;
import com.momentum.planning.domain.TimeBlock;
import com.momentum.planning.service.PlanningApplicationService;
import jakarta.servlet.http.HttpServletRequest;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/planning")
public class PlanningController {
    private final PlanningApplicationService planning;

    public PlanningController(PlanningApplicationService planning) {
        this.planning = planning;
    }

    @GetMapping("/days")
    public Map<String, Object> day(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        UUID userId = PlanningApplicationService.userId(request);
        Day day = planning.getOrCreateDay(userId, date);
        List<TimeBlock> blocks = planning.blocks(day.getId());
        List<Task> tasks = planning.tasks(day.getId());
        return Map.of("day", day, "blocks", blocks, "tasks", tasks);
    }

    @PostMapping("/days/{dayId}/blocks")
    public TimeBlock createBlock(@PathVariable UUID dayId, @RequestBody Map<String, String> body) {
        return planning.createBlock(dayId, body.get("title"), LocalTime.parse(body.get("startTime")), LocalTime.parse(body.get("endTime")));
    }

    @PostMapping("/days/{dayId}/tasks")
    public Task createTask(HttpServletRequest request, @PathVariable UUID dayId, @RequestBody Map<String, String> body) {
        UUID blockId = body.get("timeBlockId") == null ? null : UUID.fromString(body.get("timeBlockId"));
        RecurrenceType recurrence = body.get("recurrence") == null ? RecurrenceType.NONE : RecurrenceType.valueOf(body.get("recurrence"));
        return planning.createTask(
                PlanningApplicationService.userId(request),
                PlanningApplicationService.cid(request),
                dayId,
                blockId,
                body.get("title"),
                recurrence);
    }

    @PostMapping("/tasks/{taskId}/complete")
    public Task complete(HttpServletRequest request, @PathVariable UUID taskId) {
        return planning.completeTask(
                PlanningApplicationService.userId(request),
                PlanningApplicationService.cid(request),
                taskId);
    }

    @PostMapping("/days/{dayId}/close")
    public Day close(HttpServletRequest request, @PathVariable UUID dayId) {
        return planning.closeDay(
                PlanningApplicationService.userId(request),
                PlanningApplicationService.cid(request),
                dayId);
    }

    @GetMapping("/tasks/{taskId}/subtasks")
    public List<SubTask> subTasks(@PathVariable UUID taskId) {
        return planning.subTasks(taskId);
    }

    @PostMapping("/tasks/{taskId}/subtasks")
    public SubTask addSub(@PathVariable UUID taskId, @RequestBody Map<String, Object> body) {
        int order = body.get("sortOrder") == null ? 0 : ((Number) body.get("sortOrder")).intValue();
        return planning.addSubTask(taskId, String.valueOf(body.get("title")), order);
    }

    @PatchMapping("/subtasks/{id}")
    public SubTask toggle(@PathVariable UUID id, @RequestBody Map<String, Boolean> body) {
        return planning.toggleSubTask(id, Boolean.TRUE.equals(body.get("done")));
    }
}
