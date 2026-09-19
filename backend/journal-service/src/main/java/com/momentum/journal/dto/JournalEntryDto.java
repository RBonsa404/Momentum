package com.momentum.journal.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class JournalEntryDto {
    @NotNull
    private UUID id;
    @NotNull
    private UUID userId;
    @NotNull
    private LocalDate dayDate;
    private String wins;
    private String struggles;
    private String gratitude;
    @NotNull
    @Min(1)
    @Max(10)
    private int mood;
    @NotNull
    @Min(1)
    @Max(10)
    private int energy;
    private String tags;
    @NotNull
    private Instant submittedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public LocalDate getDayDate() { return dayDate; }
    public void setDayDate(LocalDate dayDate) { this.dayDate = dayDate; }
    public String getWins() { return wins; }
    public void setWins(String wins) { this.wins = wins; }
    public String getStruggles() { return struggles; }
    public void setStruggles(String struggles) { this.struggles = struggles; }
    public String getGratitude() { return gratitude; }
    public void setGratitude(String gratitude) { this.gratitude = gratitude; }
    public int getMood() { return mood; }
    public void setMood(int mood) { this.mood = mood; }
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
}