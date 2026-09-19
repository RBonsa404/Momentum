package com.momentum.streak.service;

import com.momentum.streak.domain.Streak;
import com.momentum.streak.domain.StreakRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StreakEngineTest {
    private final StreakEngine engine = new StreakEngine();

    @Test
    void submittedIncrementsStreak() {
        Streak streak = new Streak();
        streak.setUserId(UUID.randomUUID());
        StreakRule rule = new StreakRule();
        engine.onJournalSubmitted(streak, rule, LocalDate.of(2026, 9, 6));
        engine.onJournalSubmitted(streak, rule, LocalDate.of(2026, 9, 7));
        assertThat(streak.getCurrentLength()).isEqualTo(2);
        assertThat(streak.getLongestLength()).isEqualTo(2);
    }

    @Test
    void missedWithoutJokerBreaksStreak() {
        Streak streak = new Streak();
        streak.setCurrentLength(4);
        StreakRule rule = new StreakRule();
        rule.setJokersRemaining(0);
        engine.onJournalMissed(streak, rule, LocalDate.of(2026, 9, 6));
        assertThat(streak.getCurrentLength()).isZero();
        assertThat(streak.getStatus()).isEqualTo("BROKEN");
    }

    @Test
    void missedWithJokerPreservesStreak() {
        Streak streak = new Streak();
        streak.setCurrentLength(4);
        StreakRule rule = new StreakRule();
        rule.setJokersRemaining(1);
        engine.onJournalMissed(streak, rule, LocalDate.of(2026, 9, 6));
        assertThat(streak.getCurrentLength()).isEqualTo(4);
        assertThat(rule.getJokersRemaining()).isZero();
        assertThat(streak.getStatus()).isEqualTo("JOKER");
    }

    @Test
    void missedOnOffDayPreservesStreak() {
        Streak streak = new Streak();
        streak.setCurrentLength(4);
        StreakRule rule = new StreakRule();
        rule.setOffWeekdays("SATURDAY,SUNDAY");
        rule.setJokersRemaining(2);
        engine.onJournalMissed(streak, rule, LocalDate.of(2026, 9, 12)); // Saturday
        assertThat(streak.getCurrentLength()).isEqualTo(4);
        assertThat(rule.getJokersRemaining()).isEqualTo(2);
        assertThat(streak.getStatus()).isEqualTo("OFF_DAY");
    }
}
