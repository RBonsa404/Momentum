package com.momentum.server.domain.goal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private String title;
    private String specificText;
    private String measurableText;
    private String achievableText;
    private String relevantText;
    private LocalDate dueDate;
    @Column(nullable = false)
    private int progressPercent;
    private UUID linkedTaskOriginId;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
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
    public int getProgressPercent() { return progressPercent; }
    public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }
    public UUID getLinkedTaskOriginId() { return linkedTaskOriginId; }
    public void setLinkedTaskOriginId(UUID linkedTaskOriginId) { this.linkedTaskOriginId = linkedTaskOriginId; }
}
