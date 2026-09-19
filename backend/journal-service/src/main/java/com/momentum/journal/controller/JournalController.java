package com.momentum.journal.controller;

import com.momentum.common.tracing.CorrelationIds;
import com.momentum.journal.dto.JournalEntryDto;
import com.momentum.journal.dto.SubmitJournalRequest;
import com.momentum.journal.domain.JournalEntry;
import com.momentum.journal.mapper.JournalMapper;
import com.momentum.journal.service.JournalService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/journal")
public class JournalController {
    private final JournalService journalService;
    private final JournalMapper mapper;

    public JournalController(JournalService journalService, JournalMapper mapper) {
        this.journalService = journalService;
        this.mapper = mapper;
    }

    @PostMapping
    public JournalEntryDto submit(HttpServletRequest request, @Valid @RequestBody SubmitJournalRequest body) {
        UUID userId = JournalService.userId(request);
        String cid = request.getHeader(CorrelationIds.HTTP_HEADER);
        JournalEntry entry = journalService.submit(userId, cid, body);
        return mapper.toDto(entry);
    }

    @GetMapping
    public List<JournalEntryDto> history(HttpServletRequest request) {
        UUID userId = JournalService.userId(request);
        journalService.track(userId);
        return journalService.history(userId).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/day")
    public JournalEntryDto day(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        UUID userId = JournalService.userId(request);
        journalService.track(userId);
        return mapper.toDto(journalService.get(userId, date));
    }
}
