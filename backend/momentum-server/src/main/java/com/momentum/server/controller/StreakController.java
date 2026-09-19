package com.momentum.server.controller;

import com.momentum.server.config.SecurityUtils;
import com.momentum.server.domain.streak.Streak;
import com.momentum.server.domain.streak.StreakRule;
import com.momentum.server.service.StreakService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/streak")
public class StreakController {
    private final StreakService streaks;

    public StreakController(StreakService streaks) {
        this.streaks = streaks;
    }

    @GetMapping("/current")
    public Streak current() {
        return streaks.current(SecurityUtils.getUserId());
    }

    @GetMapping("/rules")
    public StreakRule rules() {
        return streaks.rules(SecurityUtils.getUserId());
    }

    @PutMapping("/rules")
    public StreakRule update(@RequestBody Map<String, Object> body) {
        UUID userId = SecurityUtils.getUserId();
        int jokers = ((Number) body.getOrDefault("jokersPerMonth", 2)).intValue();
        return streaks.updateRules(userId, String.valueOf(body.getOrDefault("offWeekdays", "")), jokers);
    }
}
