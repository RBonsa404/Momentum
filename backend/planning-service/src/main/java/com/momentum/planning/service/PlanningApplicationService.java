package com.momentum.planning.service;

import com.momentum.common.tracing.CorrelationIds;
import com.momentum.planning.domain.Day;
import com.momentum.planning.domain.RecurrenceType;
import com.momentum.planning.domain.SubTask;
import com.momentum.planning.domain.Task;
import com.momentum.planning.domain.TaskStatus;
import com.momentum.planning.domain.TimeBlock;
import com.momentum.planning.messaging.PlanningEventPublisher;
import com.momentum.planning.repository.DayRepository;
import com.momentum.planning.repository.SubTaskRepository;
import com.momentum.planning.repository.TaskRepository;
import com.momentum.planning.repository.TimeBlockRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class PlanningApplicationService {
    private final DayRepository days;
    private final TimeBlockRepository blocks;
    private final TaskRepository tasks;
    private final SubTaskRepository subTasks;
    private final RecurrenceService recurrenceService = new RecurrenceService();
    private final CarryOverService carryOverService = new CarryOverService();
    private final PlanningEventPublisher publisher;

    public PlanningApplicationService(
            DayRepository days,
            TimeBlockRepository blocks,
            TaskRepository tasks,
            SubTaskRepository subTasks,
            PlanningEventPublisher publisher) {
        this.days = days;
        this.blocks = blocks;
        this.tasks = tasks;
        this.subTasks = subTasks;
        this.publisher = publisher;
    }

    public Day getOrCreateDay(UUID userId, LocalDate date) {
        return days.findByUserIdAndDate(userId, date).orElseGet(() -> {
            Day day = new Day();
            day.setUserId(userId);
            day.setDate(date);
            day.setClosed(false);
            return days.save(day);
        });
    }

    public List<TimeBlock> blocks(UUID dayId) {
        return blocks.findByDayIdOrderByStartTimeAsc(dayId);
    }

    public List<Task> tasks(UUID dayId) {
        return tasks.findByDayId(dayId);
    }

    public List<SubTask> subTasks(UUID taskId) {
        return subTasks.findByTaskIdOrderBySortOrderAsc(taskId);
    }

    public TimeBlock createBlock(UUID dayId, String title, LocalTime start, LocalTime end) {
        TimeBlock block = new TimeBlock();
        block.setDayId(dayId);
        block.setTitle(title);
        block.setStartTime(start);
        block.setEndTime(end);
        return blocks.save(block);
    }

    public Task createTask(UUID userId, String correlationId, UUID dayId, UUID timeBlockId, String title, RecurrenceType recurrence) {
        Day day = days.findById(dayId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Task task = new Task();
        task.setDayId(dayId);
        task.setTimeBlockId(timeBlockId);
        task.setTitle(title);
        task.setRecurrence(recurrence == null ? RecurrenceType.NONE : recurrence);
        Task saved = tasks.save(task);
        publisher.taskCreated(userId, correlationId, saved, day);
        return saved;
    }

    public Task completeTask(UUID userId, String correlationId, UUID taskId) {
        Task task = tasks.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        task.setStatus(TaskStatus.DONE);
        Task saved = tasks.save(task);
        Day day = days.findById(task.getDayId()).orElseThrow();
        publisher.taskCompleted(userId, correlationId, saved, day);
        materializeRecurrence(userId, correlationId, saved, day);
        return saved;
    }

    @Transactional
    public Day closeDay(UUID userId, String correlationId, UUID dayId) {
        Day day = days.findById(dayId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (day.isClosed()) {
            return day;
        }
        day.setClosed(true);
        days.save(day);
        Day next = getOrCreateDay(userId, day.getDate().plusDays(1));
        List<Task> unfinished = tasks.findByDayIdAndStatus(day.getId(), TaskStatus.TODO);
        List<Task> postponed = carryOverService.postponeUnfinished(unfinished, next.getId());
        for (Task copy : postponed) {
            Task saved = tasks.save(copy);
            publisher.taskPostponed(userId, correlationId, saved, next);
        }
        publisher.dayClosed(userId, correlationId, day);
        return day;
    }

    public SubTask addSubTask(UUID taskId, String title, int order) {
        SubTask sub = new SubTask();
        sub.setTaskId(taskId);
        sub.setTitle(title);
        sub.setSortOrder(order);
        return subTasks.save(sub);
    }

    public SubTask toggleSubTask(UUID id, boolean done) {
        SubTask sub = subTasks.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sub.setDone(done);
        return subTasks.save(sub);
    }

    public static UUID userId(HttpServletRequest request) {
        String header = request.getHeader(CorrelationIds.USER_HEADER);
        if (header == null || header.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user");
        }
        return UUID.fromString(header);
    }

    public static String cid(HttpServletRequest request) {
        return PlanningEventPublisher.correlationOrNew(request.getHeader(CorrelationIds.HTTP_HEADER));
    }

    private void materializeRecurrence(UUID userId, String correlationId, Task source, Day day) {
        LocalDate nextDate = recurrenceService.nextOccurrence(source.getRecurrence(), day.getDate());
        if (nextDate == null) {
            return;
        }
        Day nextDay = getOrCreateDay(userId, nextDate);
        Task next = new Task();
        next.setDayId(nextDay.getId());
        next.setTitle(source.getTitle());
        next.setNotes(source.getNotes());
        next.setRecurrence(source.getRecurrence());
        next.setOriginTaskId(source.getOriginTaskId() == null ? source.getId() : source.getOriginTaskId());
        Task saved = tasks.save(next);
        publisher.taskCreated(userId, correlationId, saved, nextDay);
    }
}
