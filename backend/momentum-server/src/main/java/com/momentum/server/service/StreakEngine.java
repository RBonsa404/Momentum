package com.momentum.server.service;

import com.momentum.server.domain.streak.Streak;
import com.momentum.server.domain.streak.StreakRule;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StreakEngine {

    public void onJournalSubmitted(Streak streak, StreakRule rule, LocalDate date) {
        if (streak.getLastQualifiedDate() != null && !date.isAfter(streak.getLastQualifiedDate())) {
            return;
        }
        streak.setCurrentLength(streak.getCurrentLength() + 1);
        streak.setLastQualifiedDate(date);
        streak.setStatus("ACTIVE");
        if (streak.getCurrentLength() > streak.getLongestLength()) {
            streak.setLongestLength(streak.getCurrentLength());
        }
    }

    public void onJournalMissed(Streak streak, StreakRule rule, LocalDate date) {
        if (isOffDay(rule, date)) {
            streak.setLastQualifiedDate(date);
            streak.setStatus("OFF_DAY");
            return;
        }
        if (rule.getJokersRemaining() > 0) {
            rule.setJokersRemaining(rule.getJokersRemaining() - 1);
            streak.setLastQualifiedDate(date);
            streak.setStatus("JOKER");
            return;
        }
        streak.setCurrentLength(0);
        streak.setStatus("BROKEN");
    }

    public boolean isOffDay(StreakRule rule, LocalDate date) {
        if (rule.getOffWeekdays() == null || rule.getOffWeekdays().isBlank()) {
            return false;
        }
        Set<DayOfWeek> offs = Arrays.stream(rule.getOffWeekdays().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());
        return offs.contains(date.getDayOfWeek());
    }
}
