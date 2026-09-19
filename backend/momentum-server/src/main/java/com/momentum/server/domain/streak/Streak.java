package com.momentum.server.domain.streak;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "streaks")
public class Streak {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private UUID userId;
    @Column(nullable = false)
    private int currentLength;
    @Column(nullable = false)
    private int longestLength;
    private LocalDate lastQualifiedDate;
    @Column(nullable = false)
    private String status = "INACTIVE";

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public int getCurrentLength() { return currentLength; }
    public void setCurrentLength(int currentLength) { this.currentLength = currentLength; }
    public int getLongestLength() { return longestLength; }
    public void setLongestLength(int longestLength) { this.longestLength = longestLength; }
    public LocalDate getLastQualifiedDate() { return lastQualifiedDate; }
    public void setLastQualifiedDate(LocalDate lastQualifiedDate) { this.lastQualifiedDate = lastQualifiedDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
