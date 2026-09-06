package com.momentum.streak.service;

import com.momentum.common.event.DomainEvent;
import com.momentum.common.event.EventTypes;
import com.momentum.common.event.payload.JournalLifecyclePayload;
import com.momentum.common.event.payload.StreakUpdatedPayload;
import com.momentum.streak.domain.Streak;
import com.momentum.streak.domain.StreakRule;
import com.momentum.streak.repository.StreakRepository;
import com.momentum.streak.repository.StreakRuleRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class StreakApplicationService {
    private final StreakRepository streaks;
    private final StreakRuleRepository rules;
    private final StreakEngine engine = new StreakEngine();
    private final RabbitTemplate rabbitTemplate;

    public StreakApplicationService(StreakRepository streaks, StreakRuleRepository rules, RabbitTemplate rabbitTemplate) {
        this.streaks = streaks;
        this.rules = rules;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Streak current(UUID userId) {
        return ensureStreak(userId);
    }

    public StreakRule rules(UUID userId) {
        return ensureRule(userId);
    }

    public StreakRule updateRules(UUID userId, String offWeekdays, int jokersPerMonth) {
        StreakRule rule = ensureRule(userId);
        rule.setOffWeekdays(offWeekdays == null ? "" : offWeekdays);
        rule.setJokersPerMonth(jokersPerMonth);
        rule.setJokersRemaining(jokersPerMonth);
        return rules.save(rule);
    }

    @RabbitListener(queues = "momentum.streak.journal")
    @Transactional
    public void onJournal(DomainEvent<JournalLifecyclePayload> event) {
        if (event.getPayload() == null || event.getUserId() == null) {
            return;
        }
        Streak streak = ensureStreak(event.getUserId());
        StreakRule rule = ensureRule(event.getUserId());
        LocalDate date = event.getPayload().getDayDate();
        if (event.getPayload().isMissed() || EventTypes.JOURNAL_MISSED.equals(event.getEventType())) {
            engine.onJournalMissed(streak, rule, date);
        } else {
            engine.onJournalSubmitted(streak, rule, date);
        }
        rules.save(rule);
        streaks.save(streak);
        StreakUpdatedPayload payload = new StreakUpdatedPayload();
        payload.setStreakId(streak.getId());
        payload.setCurrentLength(streak.getCurrentLength());
        payload.setLongestLength(streak.getLongestLength());
        payload.setLastQualifiedDate(streak.getLastQualifiedDate());
        payload.setStatus(streak.getStatus());
        DomainEvent<StreakUpdatedPayload> out = new DomainEvent<>(
                EventTypes.STREAK_UPDATED, event.getCorrelationId(), event.getUserId(), payload);
        rabbitTemplate.convertAndSend(EventTypes.EXCHANGE, EventTypes.STREAK_UPDATED, out);
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
