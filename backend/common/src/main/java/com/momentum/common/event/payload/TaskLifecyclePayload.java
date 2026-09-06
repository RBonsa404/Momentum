package com.momentum.common.event.payload;

import java.time.LocalDate;
import java.util.UUID;

public class TaskLifecyclePayload {
    private UUID taskId;
    private UUID dayId;
    private LocalDate dayDate;
    private String title;
    private String status;
    private int postponedCount;
    private boolean dueUnfinished;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public UUID getDayId() {
        return dayId;
    }

    public void setDayId(UUID dayId) {
        this.dayId = dayId;
    }

    public LocalDate getDayDate() {
        return dayDate;
    }

    public void setDayDate(LocalDate dayDate) {
        this.dayDate = dayDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPostponedCount() {
        return postponedCount;
    }

    public void setPostponedCount(int postponedCount) {
        this.postponedCount = postponedCount;
    }

    public boolean isDueUnfinished() {
        return dueUnfinished;
    }

    public void setDueUnfinished(boolean dueUnfinished) {
        this.dueUnfinished = dueUnfinished;
    }
}
