package com.momentum.server.dto.stats;

import java.time.LocalDate;
import java.util.List;

public class DashboardResponse {
    private LocalDate from;
    private LocalDate to;
    private int planned;
    private int done;
    private int unfinished;
    private int postponed;
    private int missedJournals;
    private int procrastinationScore;
    private int completionRate;
    private List<DailySnapshotDto> heatmap;

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
    public int getCompletionRate() { return completionRate; }
    public void setCompletionRate(int completionRate) { this.completionRate = completionRate; }
    public List<DailySnapshotDto> getHeatmap() { return heatmap; }
    public void setHeatmap(List<DailySnapshotDto> heatmap) { this.heatmap = heatmap; }
}
