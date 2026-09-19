package com.momentum.journal.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class SubmitJournalRequest {
    private LocalDate dayDate;
    private String wins;
    private String struggles;
    private String gratitude;
    @NotNull
    @Min(1)
    @Max(10)
    private Integer mood;
    @NotNull
    @Min(1)
    @Max(10)
    private Integer energy;
    private String tags;

    public LocalDate getDayDate() { return dayDate; }
    public void setDayDate(LocalDate dayDate) { this.dayDate = dayDate; }
    public String getWins() { return wins; }
    public void setWins(String wins) { this.wins = wins; }
    public String getStruggles() { return struggles; }
    public void setStruggles(String struggles) { this.struggles = struggles; }
    public String getGratitude() { return gratitude; }
    public void setGratitude(String gratitude) { this.gratitude = gratitude; }
    public Integer getMood() { return mood; }
    public void setMood(Integer mood) { this.mood = mood; }
    public Integer getEnergy() { return energy; }
    public void setEnergy(Integer energy) { this.energy = energy; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
}