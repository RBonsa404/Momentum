package com.momentum.server.domain.streak;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "streak_rules")
public class StreakRule {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(nullable = false, unique = true)
    private UUID userId;
    @Column(nullable = false)
    private String offWeekdays = "";
    @Column(nullable = false)
    private int jokersPerMonth = 2;
    @Column(nullable = false)
    private int jokersRemaining = 2;
    private LocalDate lastJokerReset;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getOffWeekdays() { return offWeekdays; }
    public void setOffWeekdays(String offWeekdays) { this.offWeekdays = offWeekdays; }
    public int getJokersPerMonth() { return jokersPerMonth; }
    public void setJokersPerMonth(int jokersPerMonth) { this.jokersPerMonth = jokersPerMonth; }
    public int getJokersRemaining() { return jokersRemaining; }
    public void setJokersRemaining(int jokersRemaining) { this.jokersRemaining = jokersRemaining; }
    public LocalDate getLastJokerReset() { return lastJokerReset; }
    public void setLastJokerReset(LocalDate lastJokerReset) { this.lastJokerReset = lastJokerReset; }
}
