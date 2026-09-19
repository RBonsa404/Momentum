package com.momentum.server.dto.planning;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class CreateTimeBlockRequest {
    @NotBlank
    private String title;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}
