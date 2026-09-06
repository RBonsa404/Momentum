package com.momentum.common.event.payload;

import java.time.LocalDate;
import java.util.UUID;

public class StreakUpdatedPayload {
    private UUID streakId;
    private int currentLength;
    private int longestLength;
    private LocalDate lastQualifiedDate;
    private String status;

    public UUID getStreakId() {
        return streakId;
    }

    public void setStreakId(UUID streakId) {
        this.streakId = streakId;
    }

    public int getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(int currentLength) {
        this.currentLength = currentLength;
    }

    public int getLongestLength() {
        return longestLength;
    }

    public void setLongestLength(int longestLength) {
        this.longestLength = longestLength;
    }

    public LocalDate getLastQualifiedDate() {
        return lastQualifiedDate;
    }

    public void setLastQualifiedDate(LocalDate lastQualifiedDate) {
        this.lastQualifiedDate = lastQualifiedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
