package com.momentum.server.controller;

import com.momentum.server.config.SecurityUtils;
import com.momentum.server.domain.stats.DailySnapshot;
import com.momentum.server.dto.stats.DailySnapshotDto;
import com.momentum.server.dto.stats.DashboardResponse;
import com.momentum.server.repository.DailySnapshotRepository;
import com.momentum.server.service.ProcrastinationScoreCalculator;
import com.momentum.server.service.StatsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final DailySnapshotRepository snapshots;
    private final StatsService statsService;
    private final ProcrastinationScoreCalculator calculator;

    public StatsController(DailySnapshotRepository snapshots, StatsService statsService, ProcrastinationScoreCalculator calculator) {
        this.snapshots = snapshots;
        this.statsService = statsService;
        this.calculator = calculator;
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard(
            @RequestParam(defaultValue = "week") String period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        UUID userId = SecurityUtils.getUserId();
        LocalDate end = to == null ? LocalDate.now() : to;
        LocalDate start = from != null ? from : switch (period) {
            case "day" -> end;
            case "month" -> end.minusDays(29);
            default -> end.minusDays(6);
        };
        List<DailySnapshot> rows = snapshots.findByUserIdAndDayDateBetweenOrderByDayDateAsc(userId, start, end);
        int planned = rows.stream().mapToInt(DailySnapshot::getPlanned).sum();
        int done = rows.stream().mapToInt(DailySnapshot::getDone).sum();
        int unfinished = rows.stream().mapToInt(DailySnapshot::getUnfinished).sum();
        int postponed = rows.stream().mapToInt(DailySnapshot::getPostponed).sum();
        int missed = rows.stream().mapToInt(DailySnapshot::getMissedJournals).sum();
        int expectedJournals = (int) start.datesUntil(end.plusDays(1)).count();
        int score = calculator.score(unfinished, missed, postponed, calculator.expectedItems(planned, expectedJournals));
        
        DashboardResponse response = new DashboardResponse();
        response.setFrom(start);
        response.setTo(end);
        response.setPlanned(planned);
        response.setDone(done);
        response.setUnfinished(unfinished);
        response.setPostponed(postponed);
        response.setMissedJournals(missed);
        response.setProcrastinationScore(score);
        response.setHeatmap(rows.stream().map(statsService::toDto).toList());
        response.setCompletionRate(planned == 0 ? 0 : (int) Math.round(100.0 * done / planned));
        return response;
    }

    @GetMapping("/streak")
    public List<DailySnapshotDto> streak(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        UUID userId = SecurityUtils.getUserId();
        LocalDate end = to == null ? LocalDate.now() : to;
        LocalDate start = from != null ? from : end.minusDays(29);
        return snapshots.findByUserIdAndDayDateBetweenOrderByDayDateAsc(userId, start, end)
                .stream()
                .map(statsService::toDto)
                .toList();
    }

    @GetMapping("/procrastination")
    public DashboardResponse procrastination(
            @RequestParam(defaultValue = "week") String period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return dashboard(period, from, to);
    }
}
