package com.momentum.server.controller;

import com.momentum.server.config.SecurityUtils;
import com.momentum.server.dto.journal.JournalEntryDto;
import com.momentum.server.dto.journal.SubmitJournalRequest;
import com.momentum.server.service.JournalService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    @PostMapping({"", "/submit"})
    public JournalEntryDto submit(@Valid @RequestBody SubmitJournalRequest body) {
        UUID userId = SecurityUtils.getUserId();
        return journalService.toDto(journalService.submit(userId, body));
    }

    @GetMapping({"", "/history"})
    public List<JournalEntryDto> history() {
        UUID userId = SecurityUtils.getUserId();
        return journalService.history(userId).stream().map(journalService::toDto).toList();
    }

    @GetMapping("/{date}")
    public JournalEntryDto get(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        UUID userId = SecurityUtils.getUserId();
        return journalService.toDto(journalService.get(userId, date));
    }
}
