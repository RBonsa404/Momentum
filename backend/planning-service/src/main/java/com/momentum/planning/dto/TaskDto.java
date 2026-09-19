package com.momentum.planning.dto;

import com.momentum.planning.domain.RecurrenceType;
import com.momentum.planning.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class TaskDto {
    @NotNull
    private UUID id;
    @NotNull
    private UUID dayId;
    private UUID timeBlockId;
    @NotBlank
    private String title;
    private String notes;
    @NotNull
    private TaskStatus status;
    @NotNull
    private RecurrenceType recurrence;
    @NotNull
    private int postponedCount;
    private UUID originTaskId;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getDayId() { return dayId; }
    public void setDayId(UUID dayId) { this.dayId = dayId; }
    public UUID getTimeBlockId() { return timeBlockId; }
    public void setTimeBlockId(UUID timeBlockId) { this.timeBlockId = timeBlockId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public RecurrenceType getRecurrence() { return recurrence; }
    public void setRecurrence(RecurrenceType recurrence) { this.recurrence = recurrence; }
    public int getPostponedCount() { return postponedCount; }
    public void setPostponedCount(int postponedCount) { this.postponedCount = postponedCount; }
    public UUID getOriginTaskId() { return originTaskId; }
    public void setOriginTaskId(UUID originTaskId) { this.originTaskId = originTaskId; }
}