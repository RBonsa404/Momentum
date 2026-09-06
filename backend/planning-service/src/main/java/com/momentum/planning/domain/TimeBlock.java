package com.momentum.planning.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "time_blocks")
public class TimeBlock {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(nullable = false)
    private UUID dayId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
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
