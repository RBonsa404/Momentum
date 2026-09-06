package com.momentum.journal.controller;

import com.momentum.common.tracing.CorrelationIds;
import com.momentum.journal.domain.JournalEntry;
import com.momentum.journal.service.JournalService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/journal")
public class JournalController {
    private final JournalService journalService;

    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    @PostMapping
    public JournalEntry submit(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        UUID userId = JournalService.userId(request);
        String cid = request.getHeader(CorrelationIds.HTTP_HEADER);
        return journalService.submit(userId, cid, body);
    }

    @GetMapping
    public List<JournalEntry> history(HttpServletRequest request) {
        UUID userId = JournalService.userId(request);
        journalService.track(userId);
        return journalService.history(userId);
    }

    @GetMapping("/day")
    public JournalEntry day(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        UUID userId = JournalService.userId(request);
        journalService.track(userId);
        return journalService.get(userId, date);
    }
}
