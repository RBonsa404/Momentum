package com.momentum.server.domain.journal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "journal_entries")
public class JournalEntry {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private LocalDate dayDate;
    @Column(nullable = false)
    private String wins;
    private String struggles;
    private String gratitude;
    @Column(nullable = false)
    private int mood;
    @Column(nullable = false)
    private int energy;
    private String tags;
    @Column(nullable = false)
    private Instant submittedAt = Instant.now();

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
    public Instant getCreatedAt() { return submittedAt; }
}
