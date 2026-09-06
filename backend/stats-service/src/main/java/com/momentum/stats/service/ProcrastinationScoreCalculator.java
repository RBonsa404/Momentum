package com.momentum.stats.service;

public class ProcrastinationScoreCalculator {

    public int score(int unfinishedDue, int missedJournals, int repeatedPostpones, int expectedItems) {
        int expected = Math.max(1, expectedItems);
        double raw = 100.0 * (unfinishedDue + missedJournals + repeatedPostpones) / expected;
        return (int) Math.round(Math.max(0, Math.min(100, raw)));
    }

    public int expectedItems(int plannedTasks, int expectedJournals) {
        return plannedTasks + expectedJournals;
    }
}
