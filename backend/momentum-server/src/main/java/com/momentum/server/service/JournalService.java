package com.momentum.server.service;

import com.momentum.server.domain.journal.JournalEntry;
import com.momentum.server.domain.journal.TrackedUser;
import com.momentum.server.dto.journal.JournalEntryDto;
import com.momentum.server.dto.journal.SubmitJournalRequest;
import com.momentum.server.repository.JournalEntryRepository;
import com.momentum.server.repository.TrackedUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class JournalService {
    private final JournalEntryRepository entries;
    private final TrackedUserRepository trackedUsers;
    private final StreakService streakService;
    private final StatsService statsService;
    private final NotificationService notificationService;

    public JournalService(
            JournalEntryRepository entries,
            TrackedUserRepository trackedUsers,
            StreakService streakService,
            StatsService statsService,
            NotificationService notificationService) {
        this.entries = entries;
        this.trackedUsers = trackedUsers;
        this.streakService = streakService;
        this.statsService = statsService;
        this.notificationService = notificationService;
    }

    public void track(UUID userId) {
        if (!trackedUsers.existsById(userId)) {
            TrackedUser tracked = new TrackedUser();
            tracked.setUserId(userId);
            trackedUsers.save(tracked);
        }
    }

    @Transactional
    public JournalEntry submit(UUID userId, SubmitJournalRequest request) {
        LocalDate date = request.getDayDate() == null ? LocalDate.now() : request.getDayDate();
        JournalEntry entry = entries.findByUserIdAndDayDate(userId, date).orElseGet(JournalEntry::new);
        entry.setUserId(userId);
        entry.setDayDate(date);
        entry.setWins(request.getWins() == null ? "" : request.getWins());
        entry.setStruggles(request.getStruggles());
        entry.setGratitude(request.getGratitude());
        entry.setMood(request.getMood() == null ? 0 : request.getMood());
        entry.setEnergy(request.getEnergy() == null ? 0 : request.getEnergy());
        entry.setTags(request.getTags());
        track(userId);
        JournalEntry saved = entries.save(entry);

        // Direct calls replacing RabbitMQ events
        streakService.onJournalSubmitted(userId, saved.getDayDate());
        statsService.onJournalSubmitted(userId, saved.getDayDate(), saved.getMood(), saved.getEnergy());

        return saved;
    }

    public List<JournalEntry> history(UUID userId) {
        return entries.findByUserIdOrderByDayDateDesc(userId);
    }

    public JournalEntry get(UUID userId, LocalDate date) {
        return entries.findByUserIdAndDayDate(userId, date)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Journal entry not found"));
    }

    @Scheduled(cron = "${momentum.journal.missed-cron:0 59 23 * * *}")
    public void emitMissedForToday() {
        LocalDate today = LocalDate.now();
        trackedUsers.findAll().forEach(user -> markMissed(user.getUserId(), today));
    }

    @Transactional
    public void markMissed(UUID userId, LocalDate date) {
        if (entries.existsByUserIdAndDayDate(userId, date)) {
            return;
        }
        streakService.onJournalMissed(userId, date);
        statsService.onJournalMissed(userId, date);
        notificationService.onJournalMissed(userId);
    }

    public JournalEntryDto toDto(JournalEntry entry) {
        JournalEntryDto dto = new JournalEntryDto();
        dto.setId(entry.getId());
        dto.setUserId(entry.getUserId());
        dto.setDayDate(entry.getDayDate());
        dto.setWins(entry.getWins());
        dto.setStruggles(entry.getStruggles());
        dto.setGratitude(entry.getGratitude());
        dto.setMood(entry.getMood());
        dto.setEnergy(entry.getEnergy());
        dto.setTags(entry.getTags());
        dto.setCreatedAt(entry.getCreatedAt());
        return dto;
    }
}
