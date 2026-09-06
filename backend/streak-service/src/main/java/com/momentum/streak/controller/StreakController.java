package com.momentum.streak.controller;

import com.momentum.common.tracing.CorrelationIds;
import com.momentum.streak.domain.Streak;
import com.momentum.streak.domain.StreakRule;
import com.momentum.streak.service.StreakApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/streak")
public class StreakController {
    private final StreakApplicationService streaks;

    public StreakController(StreakApplicationService streaks) {
        this.streaks = streaks;
    }

    @GetMapping("/current")
    public Streak current(HttpServletRequest request) {
        return streaks.current(userId(request));
    }

    @GetMapping("/rules")
    public StreakRule rules(HttpServletRequest request) {
        return streaks.rules(userId(request));
    }

    @PutMapping("/rules")
    public StreakRule update(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        int jokers = ((Number) body.getOrDefault("jokersPerMonth", 2)).intValue();
        return streaks.updateRules(userId(request), String.valueOf(body.getOrDefault("offWeekdays", "")), jokers);
    }

    private UUID userId(HttpServletRequest request) {
        String header = request.getHeader(CorrelationIds.USER_HEADER);
        if (header == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return UUID.fromString(header);
    }
}
