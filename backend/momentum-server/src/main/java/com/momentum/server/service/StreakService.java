package com.momentum.server.service;

import com.momentum.server.domain.streak.Streak;
import com.momentum.server.domain.streak.StreakRule;
import com.momentum.server.repository.StreakRepository;
import com.momentum.server.repository.StreakRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class StreakService {
    private final StreakRepository streaks;
    private final StreakRuleRepository rules;
    private final StreakEngine engine;
    private final StatsService statsService;

    public StreakService(StreakRepository streaks, StreakRuleRepository rules, StreakEngine engine, StatsService statsService) {
        this.streaks = streaks;
        this.rules = rules;
        this.engine = engine;
        this.statsService = statsService;
    }

    public Streak current(UUID userId) {
        return ensureStreak(userId);
    }

    public StreakRule rules(UUID userId) {
        return ensureRule(userId);
    }

    @Transactional
    public StreakRule updateRules(UUID userId, String offWeekdays, int jokersPerMonth) {
        StreakRule rule = ensureRule(userId);
        rule.setOffWeekdays(offWeekdays == null ? "" : offWeekdays);
        rule.setJokersPerMonth(jokersPerMonth);
        rule.setJokersRemaining(jokersPerMonth);
        return rules.save(rule);
    }

    @Transactional
    public void onJournalSubmitted(UUID userId, LocalDate date) {
        Streak streak = ensureStreak(userId);
        StreakRule rule = ensureRule(userId);
        engine.onJournalSubmitted(streak, rule, date);
        rules.save(rule);
        streaks.save(streak);
        statsService.onStreakUpdated(userId, date, streak.getCurrentLength());
    }

    @Transactional
    public void onJournalMissed(UUID userId, LocalDate date) {
        Streak streak = ensureStreak(userId);
        StreakRule rule = ensureRule(userId);
        engine.onJournalMissed(streak, rule, date);
        rules.save(rule);
        streaks.save(streak);
        statsService.onStreakUpdated(userId, date, streak.getCurrentLength());
    }

    private Streak ensureStreak(UUID userId) {
        return streaks.findByUserId(userId).orElseGet(() -> {
            Streak streak = new Streak();
            streak.setUserId(userId);
            return streaks.save(streak);
        });
    }

    private StreakRule ensureRule(UUID userId) {
        return rules.findByUserId(userId).orElseGet(() -> {
            StreakRule rule = new StreakRule();
            rule.setUserId(userId);
            return rules.save(rule);
        });
    }
}
