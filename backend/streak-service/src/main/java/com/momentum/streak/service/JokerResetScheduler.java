package com.momentum.streak.service;

import com.momentum.streak.domain.StreakRule;
import com.momentum.streak.repository.StreakRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class JokerResetScheduler {
    private static final Logger log = LoggerFactory.getLogger(JokerResetScheduler.class);
    
    private final StreakRuleRepository rules;

    public JokerResetScheduler(StreakRuleRepository rules) {
        this.rules = rules;
    }

    @Scheduled(cron = "0 0 0 1 * ?", zone = "UTC")
    @Transactional
    public void resetJokersMonthly() {
        LocalDate today = LocalDate.now();
        log.info("Starting monthly joker reset for date: {}", today);
        
        List<StreakRule> allRules = rules.findAll();
        log.info("Found {} streak rules to reset jokers", allRules.size());
        
        for (StreakRule rule : allRules) {
            rule.setJokersRemaining(rule.getJokersPerMonth());
            rule.setLastJokerReset(today);
            rules.save(rule);
        }
        
        log.info("Completed monthly joker reset for {} rules", allRules.size());
    }
}