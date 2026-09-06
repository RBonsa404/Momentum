package com.momentum.planning.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(nullable = false)
    private UUID dayId;
    private UUID timeBlockId;
    @Column(nullable = false)
    private String title;
    private String notes;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecurrenceType recurrence = RecurrenceType.NONE;
    @Column(nullable = false)
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
