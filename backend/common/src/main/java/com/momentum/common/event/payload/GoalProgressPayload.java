package com.momentum.common.event.payload;

import java.util.UUID;

public class GoalProgressPayload {
    private UUID goalId;
    private int progressPercent;

    public UUID getGoalId() {
        return goalId;
    }

    public void setGoalId(UUID goalId) {
        this.goalId = goalId;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }
}