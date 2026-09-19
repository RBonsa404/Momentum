package com.momentum.server.domain.stats;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "daily_snapshots")
public class DailySnapshot {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private LocalDate dayDate;
    @Column(nullable = false)
    private int planned;
    @Column(nullable = false)
    private int done;
    @Column(nullable = false)
    private int unfinished;
    @Column(nullable = false)
    private int postponed;
    @Column(nullable = false)
    private int missedJournals;
    @Column(nullable = false)
    private int submittedJournals;
    private Integer mood;
    private Integer energy;
    @Column(nullable = false)
    private int streakLength;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public LocalDate getDayDate() { return dayDate; }
    public void setDayDate(LocalDate dayDate) { this.dayDate = dayDate; }
    public int getPlanned() { return planned; }
    public void setPlanned(int planned) { this.planned = planned; }
    public int getDone() { return done; }
    public void setDone(int done) { this.done = done; }
    public int getUnfinished() { return unfinished; }
    public void setUnfinished(int unfinished) { this.unfinished = unfinished; }
    public int getPostponed() { return postponed; }
    public void setPostponed(int postponed) { this.postponed = postponed; }
    public int getMissedJournals() { return missedJournals; }
    public void setMissedJournals(int missedJournals) { this.missedJournals = missedJournals; }
    public int getSubmittedJournals() { return submittedJournals; }
    public void setSubmittedJournals(int submittedJournals) { this.submittedJournals = submittedJournals; }
    public Integer getMood() { return mood; }
    public void setMood(Integer mood) { this.mood = mood; }
    public Integer getEnergy() { return energy; }
    public void setEnergy(Integer energy) { this.energy = energy; }
    public int getStreakLength() { return streakLength; }
    public void setStreakLength(int streakLength) { this.streakLength = streakLength; }
}
