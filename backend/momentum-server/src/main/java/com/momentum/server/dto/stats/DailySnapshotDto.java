package com.momentum.server.dto.stats;

import java.time.LocalDate;
import java.util.UUID;

public class DailySnapshotDto {
    private UUID id;
    private UUID userId;
    private LocalDate dayDate;
    private int planned;
    private int done;
    private int unfinished;
    private int postponed;
    private int missedJournals;
    private int submittedJournals;
    private String mood;
    private String energy;
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
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    public String getEnergy() { return energy; }
    public void setEnergy(String energy) { this.energy = energy; }
    public int getStreakLength() { return streakLength; }
    public void setStreakLength(int streakLength) { this.streakLength = streakLength; }
}
