package com.momentum.planning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.UUID;

public class TimeBlockDto {
    @NotNull
    private UUID id;
    @NotNull
    private UUID dayId;
    @NotBlank
    private String title;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getDayId() { return dayId; }
    public void setDayId(UUID dayId) { this.dayId = dayId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}