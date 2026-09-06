package com.momentum.stats.controller;

import com.momentum.common.tracing.CorrelationIds;
import com.momentum.stats.domain.DailySnapshot;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/stats")
public class DashboardController {
    private final DailySnapshotRepository snapshots;
    private final ProcrastinationScoreCalculator calculator = new ProcrastinationScoreCalculator();

    public DashboardController(DailySnapshotRepository snapshots) {
        this.snapshots = snapshots;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(
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
        Map<String, Object> body = new HashMap<>();
        body.put("from", start);
        body.put("to", end);
        body.put("planned", planned);
        body.put("done", done);
        body.put("unfinished", unfinished);
        body.put("postponed", postponed);
        body.put("missedJournals", missed);
        body.put("procrastinationScore", score);
        body.put("heatmap", rows);
        body.put("completionRate", planned == 0 ? 0 : Math.round(100.0 * done / planned));
        return body;
    }

    private UUID userId(HttpServletRequest request) {
        String header = request.getHeader(CorrelationIds.USER_HEADER);
        if (header == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return UUID.fromString(header);
    }
}
