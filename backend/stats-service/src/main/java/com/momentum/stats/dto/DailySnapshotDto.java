package com.momentum.stats.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public class DailySnapshotDto {
    @NotNull
    private UUID id;
    @NotNull
    private UUID userId;
    @NotNull
    private LocalDate dayDate;
    @NotNull
    private int planned;
    @NotNull
    private int done;
    @NotNull
    private int unfinished;
    @NotNull
    private int postponed;
    @NotNull
    private int missedJournals;
    @NotNull
    private int submittedJournals;
    private Integer mood;
    private Integer energy;
    @NotNull
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