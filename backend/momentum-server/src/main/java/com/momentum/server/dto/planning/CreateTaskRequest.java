package com.momentum.server.dto.planning;

import com.momentum.server.domain.planning.RecurrenceType;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class CreateTaskRequest {
    @NotBlank
    private String title;
    private String notes;
    private UUID timeBlockId;
    private RecurrenceType recurrence;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public UUID getTimeBlockId() { return timeBlockId; }
    public void setTimeBlockId(UUID timeBlockId) { this.timeBlockId = timeBlockId; }
    public RecurrenceType getRecurrence() { return recurrence; }
    public void setRecurrence(RecurrenceType recurrence) { this.recurrence = recurrence; }
}
