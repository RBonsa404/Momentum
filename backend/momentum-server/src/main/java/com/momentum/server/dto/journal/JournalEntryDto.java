package com.momentum.server.dto.journal;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class JournalEntryDto {
    private UUID id;
    private UUID userId;
    private LocalDate dayDate;
    private String wins;
    private String struggles;
    private String gratitude;
    private Integer mood;
    private Integer energy;
    private String tags;
    private Instant createdAt;

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
    public Integer getMood() { return mood; }
    public void setMood(Integer mood) { this.mood = mood; }
    public Integer getEnergy() { return energy; }
    public void setEnergy(Integer energy) { this.energy = energy; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
