package com.momentum.common.event;

public final class EventTypes {
    public static final String EXCHANGE = "momentum.events";
    public static final String DLX = "momentum.events.dlx";
    public static final String DLQ = "momentum.events.dlq";

    public static final String TASK_CREATED = "momentum.planning.task-created.v1";
    public static final String TASK_COMPLETED = "momentum.planning.task-completed.v1";
    public static final String TASK_POSTPONED = "momentum.planning.task-postponed.v1";
    public static final String DAY_CLOSED = "momentum.planning.day-closed.v1";

    public static final String JOURNAL_SUBMITTED = "momentum.journal.submitted.v1";
    public static final String JOURNAL_MISSED = "momentum.journal.missed.v1";

    public static final String STREAK_UPDATED = "momentum.streak.updated.v1";
    public static final String GOAL_PROGRESS = "momentum.goal.progress.v1";

    private EventTypes() {
    }
}
