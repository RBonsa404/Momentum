package com.momentum.server.service;

import com.momentum.server.domain.planning.Day;
import com.momentum.server.domain.planning.RecurrenceType;
import com.momentum.server.domain.planning.SubTask;
import com.momentum.server.domain.planning.Task;
import com.momentum.server.domain.planning.TaskStatus;
import com.momentum.server.domain.planning.TimeBlock;
import com.momentum.server.dto.planning.DayDto;
import com.momentum.server.dto.planning.SubTaskDto;
import com.momentum.server.dto.planning.TaskDto;
import com.momentum.server.dto.planning.TimeBlockDto;
import com.momentum.server.repository.DayRepository;
import com.momentum.server.repository.SubTaskRepository;
import com.momentum.server.repository.TaskRepository;
import com.momentum.server.repository.TimeBlockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class PlanningService {
    private final DayRepository days;
    private final TimeBlockRepository blocks;
    private final TaskRepository tasks;
    private final SubTaskRepository subTasks;
    private final RecurrenceService recurrenceService;
    private final CarryOverService carryOverService;
    private final StatsService statsService;
    private final NotificationService notificationService;

    public PlanningService(
            DayRepository days,
            TimeBlockRepository blocks,
            TaskRepository tasks,
            SubTaskRepository subTasks,
            RecurrenceService recurrenceService,
            CarryOverService carryOverService,
            StatsService statsService,
            NotificationService notificationService) {
        this.days = days;
        this.blocks = blocks;
        this.tasks = tasks;
        this.subTasks = subTasks;
        this.recurrenceService = recurrenceService;
        this.carryOverService = carryOverService;
        this.statsService = statsService;
        this.notificationService = notificationService;
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

    @Transactional
    public TimeBlock createBlock(UUID dayId, String title, LocalTime start, LocalTime end) {
        TimeBlock block = new TimeBlock();
        block.setDayId(dayId);
        block.setTitle(title);
        block.setStartTime(start);
        block.setEndTime(end);
        return blocks.save(block);
    }

    @Transactional
    public Task createTask(UUID userId, UUID dayId, UUID timeBlockId, String title, String notes, RecurrenceType recurrence) {
        Day day = days.findById(dayId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"));
        Task task = new Task();
        task.setDayId(dayId);
        task.setTimeBlockId(timeBlockId);
        task.setTitle(title);
        task.setNotes(notes);
        task.setRecurrence(recurrence == null ? RecurrenceType.NONE : recurrence);
        Task saved = tasks.save(task);

        // Direct call replacing RabbitMQ event
        statsService.onTaskCreated(userId, day.getDate());

        return saved;
    }

    @Transactional
    public Task completeTask(UUID userId, UUID taskId) {
        Task task = tasks.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        task.setStatus(TaskStatus.DONE);
        Task saved = tasks.save(task);
        Day day = days.findById(task.getDayId()).orElseThrow();

        // Direct call replacing RabbitMQ event
        statsService.onTaskCompleted(userId, day.getDate());

        materializeRecurrence(userId, saved, day);
        return saved;
    }

    @Transactional
    public Day closeDay(UUID userId, UUID dayId) {
        Day day = days.findById(dayId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"));
        if (day.isClosed()) {
            return day;
        }
        day.setClosed(true);
        days.save(day);
        Day next = getOrCreateDay(userId, day.getDate().plusDays(1));
        List<Task> unfinished = tasks.findByDayIdAndStatus(day.getId(), TaskStatus.TODO);
        List<Task> postponed = carryOverService.postponeUnfinished(unfinished, next.getId());
        for (Task copy : postponed) {
            tasks.save(copy);
            statsService.onTaskPostponed(userId, next.getDate());
        }

        // Direct calls replacing RabbitMQ events
        statsService.onTaskCreated(userId, next.getDate()); // Or day closed stats update
        notificationService.onDayClosed(userId);

        return day;
    }

    @Transactional
    public SubTask addSubTask(UUID taskId, String title, int order) {
        SubTask sub = new SubTask();
        sub.setTaskId(taskId);
        sub.setTitle(title);
        sub.setSortOrder(order);
        return subTasks.save(sub);
    }

    @Transactional
    public SubTask toggleSubTask(UUID id, boolean done) {
        SubTask sub = subTasks.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubTask not found"));
        sub.setDone(done);
        return subTasks.save(sub);
    }

    private void materializeRecurrence(UUID userId, Task source, Day day) {
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
        tasks.save(next);
        statsService.onTaskCreated(userId, nextDay.getDate());
    }

    public DayDto toDto(Day day) {
        DayDto dto = new DayDto();
        dto.setId(day.getId());
        dto.setUserId(day.getUserId());
        dto.setDate(day.getDate());
        dto.setClosed(day.isClosed());
        return dto;
    }

    public TimeBlockDto toDto(TimeBlock block) {
        TimeBlockDto dto = new TimeBlockDto();
        dto.setId(block.getId());
        dto.setDayId(block.getDayId());
        dto.setTitle(block.getTitle());
        dto.setStartTime(block.getStartTime());
        dto.setEndTime(block.getEndTime());
        return dto;
    }

    public TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setDayId(task.getDayId());
        dto.setTimeBlockId(task.getTimeBlockId());
        dto.setTitle(task.getTitle());
        dto.setNotes(task.getNotes());
        dto.setStatus(task.getStatus());
        dto.setRecurrence(task.getRecurrence());
        dto.setPostponedCount(task.getPostponedCount());
        dto.setOriginTaskId(task.getOriginTaskId());
        return dto;
    }

    public SubTaskDto toDto(SubTask subTask) {
        SubTaskDto dto = new SubTaskDto();
        dto.setId(subTask.getId());
        dto.setTaskId(subTask.getTaskId());
        dto.setTitle(subTask.getTitle());
        dto.setDone(subTask.isDone());
        dto.setSortOrder(subTask.getSortOrder());
        return dto;
    }
}
