package com.momentum.goal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public class MilestoneDto {
    @NotNull
    private UUID id;
    @NotNull
    private UUID goalId;
    @NotBlank
    private String title;
    private LocalDate dueDate;
    @NotNull
    private boolean done;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getGoalId() { return goalId; }
    public void setGoalId(UUID goalId) { this.goalId = goalId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
}