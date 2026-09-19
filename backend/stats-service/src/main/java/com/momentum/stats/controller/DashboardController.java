package com.momentum.stats.controller;

import com.momentum.common.tracing.CorrelationIds;
import com.momentum.stats.domain.DailySnapshot;
import com.momentum.stats.dto.DashboardResponse;
import com.momentum.stats.dto.DailySnapshotDto;
import com.momentum.stats.mapper.StatsMapper;
import com.momentum.stats.repository.DailySnapshotRepository;
import com.momentum.stats.service.ProcrastinationScoreCalculator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stats")
public class DashboardController {
    private final DailySnapshotRepository snapshots;
    private final StatsMapper mapper;
    private final ProcrastinationScoreCalculator calculator = new ProcrastinationScoreCalculator();

    public DashboardController(DailySnapshotRepository snapshots, StatsMapper mapper) {
        this.snapshots = snapshots;
        this.mapper = mapper;
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard(
            HttpServletRequest request,
            @RequestParam(defaultValue = "week") String period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        UUID userId = userId(request);
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
        response.setHeatmap(rows.stream().map(mapper::toDto).toList());
        response.setCompletionRate(planned == 0 ? 0 : (int) Math.round(100.0 * done / planned));
        return response;
    }

    @GetMapping("/streak")
    public List<DailySnapshotDto> streak(
            HttpServletRequest request,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        UUID userId = userId(request);
        LocalDate end = to == null ? LocalDate.now() : to;
        LocalDate start = from != null ? from : end.minusDays(29);
        return snapshots.findByUserIdAndDayDateBetweenOrderByDayDateAsc(userId, start, end)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/procrastination")
    public DashboardResponse procrastination(
            HttpServletRequest request,
            @RequestParam(defaultValue = "week") String period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return dashboard(request, period, from, to);
    }

    private UUID userId(HttpServletRequest request) {
        String header = request.getHeader(CorrelationIds.USER_HEADER);
        if (header == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return UUID.fromString(header);
    }
}
