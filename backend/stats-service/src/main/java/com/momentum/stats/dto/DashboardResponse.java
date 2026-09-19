package com.momentum.stats.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class DashboardResponse {
    @NotNull
    private LocalDate from;
    @NotNull
    private LocalDate to;
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
    private int procrastinationScore;
    @NotNull
    private List<DailySnapshotDto> heatmap;
    @NotNull
    private int completionRate;

    public LocalDate getFrom() { return from; }
    public void setFrom(LocalDate from) { this.from = from; }
    public LocalDate getTo() { return to; }
    public void setTo(LocalDate to) { this.to = to; }
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
    public int getProcrastinationScore() { return procrastinationScore; }
    public void setProcrastinationScore(int procrastinationScore) { this.procrastinationScore = procrastinationScore; }
    public List<DailySnapshotDto> getHeatmap() { return heatmap; }
    public void setHeatmap(List<DailySnapshotDto> heatmap) { this.heatmap = heatmap; }
    public int getCompletionRate() { return completionRate; }
    public void setCompletionRate(int completionRate) { this.completionRate = completionRate; }
}