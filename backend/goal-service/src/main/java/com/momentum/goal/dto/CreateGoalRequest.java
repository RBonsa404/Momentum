package com.momentum.goal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.UUID;

public class CreateGoalRequest {
    @NotBlank
    private String title;
    private String specificText;
    private String measurableText;
    private String achievableText;
    private String relevantText;
    private LocalDate dueDate;
    private UUID linkedTaskOriginId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSpecificText() { return specificText; }
    public void setSpecificText(String specificText) { this.specificText = specificText; }
    public String getMeasurableText() { return measurableText; }
    public void setMeasurableText(String measurableText) { this.measurableText = measurableText; }
    public String getAchievableText() { return achievableText; }
    public void setAchievableText(String achievableText) { this.achievableText = achievableText; }
    public String getRelevantText() { return relevantText; }
    public void setRelevantText(String relevantText) { this.relevantText = relevantText; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public UUID getLinkedTaskOriginId() { return linkedTaskOriginId; }
    public void setLinkedTaskOriginId(UUID linkedTaskOriginId) { this.linkedTaskOriginId = linkedTaskOriginId; }
}