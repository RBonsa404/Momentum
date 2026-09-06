package com.momentum.common.event.payload;

import java.time.LocalDate;
import java.util.UUID;

public class JournalLifecyclePayload {
    private UUID journalEntryId;
    private LocalDate dayDate;
    private Integer mood;
    private Integer energy;
    private boolean missed;

    public UUID getJournalEntryId() {
        return journalEntryId;
    }

    public void setJournalEntryId(UUID journalEntryId) {
        this.journalEntryId = journalEntryId;
    }

    public LocalDate getDayDate() {
        return dayDate;
    }

    public void setDayDate(LocalDate dayDate) {
        this.dayDate = dayDate;
    }

    public Integer getMood() {
        return mood;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }

    public Integer getEnergy() {
        return energy;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public boolean isMissed() {
        return missed;
    }

    public void setMissed(boolean missed) {
        this.missed = missed;
    }
}
