package com.momentum.journal.service;

import com.momentum.common.event.DomainEvent;
import com.momentum.common.event.EventTypes;
import com.momentum.common.event.payload.JournalLifecyclePayload;
import com.momentum.common.tracing.CorrelationIds;
import com.momentum.journal.domain.JournalEntry;
import com.momentum.journal.domain.TrackedUser;
import com.momentum.journal.dto.SubmitJournalRequest;
import com.momentum.journal.repository.JournalEntryRepository;
import com.momentum.journal.repository.TrackedUserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class JournalService {
    private final JournalEntryRepository entries;
    private final TrackedUserRepository trackedUsers;
    private final RabbitTemplate rabbitTemplate;

    public JournalService(JournalEntryRepository entries, TrackedUserRepository trackedUsers, RabbitTemplate rabbitTemplate) {
        this.entries = entries;
        this.trackedUsers = trackedUsers;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void track(UUID userId) {
        if (!trackedUsers.existsById(userId)) {
            TrackedUser tracked = new TrackedUser();
            tracked.setUserId(userId);
            trackedUsers.save(tracked);
        }
    }

    public JournalEntry submit(UUID userId, String correlationId, SubmitJournalRequest request) {
        LocalDate date = request.getDayDate() == null ? LocalDate.now() : request.getDayDate();
        JournalEntry entry = entries.findByUserIdAndDayDate(userId, date).orElseGet(JournalEntry::new);
        entry.setUserId(userId);
        entry.setDayDate(date);
        entry.setWins(request.getWins() == null ? "" : request.getWins());
        entry.setStruggles(request.getStruggles());
        entry.setGratitude(request.getGratitude());
        entry.setMood(request.getMood());
        entry.setEnergy(request.getEnergy());
        entry.setTags(request.getTags());
        track(userId);
        JournalEntry saved = entries.save(entry);
        publish(EventTypes.JOURNAL_SUBMITTED, userId, correlationId, saved, false);
        return saved;
    }

    public List<JournalEntry> history(UUID userId) {
        return entries.findByUserIdOrderByDayDateDesc(userId);
    }

    public JournalEntry get(UUID userId, LocalDate date) {
        return entries.findByUserIdAndDayDate(userId, date)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Scheduled(cron = "${momentum.journal.missed-cron:0 59 23 * * *}")
    public void emitMissedForToday() {
        LocalDate today = LocalDate.now();
        String cid = UUID.randomUUID().toString();
        trackedUsers.findAll().forEach(user -> markMissed(user.getUserId(), today, cid));
    }

    public void markMissed(UUID userId, LocalDate date, String correlationId) {
        if (entries.existsByUserIdAndDayDate(userId, date)) {
            return;
        }
        JournalLifecyclePayload payload = new JournalLifecyclePayload();
        payload.setDayDate(date);
        payload.setMissed(true);
        DomainEvent<JournalLifecyclePayload> event = new DomainEvent<>(EventTypes.JOURNAL_MISSED, correlationId, userId, payload);
        rabbitTemplate.convertAndSend(EventTypes.EXCHANGE, EventTypes.JOURNAL_MISSED, event);
    }

    private void publish(String type, UUID userId, String cid, JournalEntry entry, boolean missed) {
        JournalLifecyclePayload payload = new JournalLifecyclePayload();
        payload.setJournalEntryId(entry.getId());
        payload.setDayDate(entry.getDayDate());
        payload.setMood(entry.getMood());
        payload.setEnergy(entry.getEnergy());
        payload.setMissed(missed);
        DomainEvent<JournalLifecyclePayload> event = new DomainEvent<>(type, cid, userId, payload);
        rabbitTemplate.convertAndSend(EventTypes.EXCHANGE, type, event);
    }

    public static UUID userId(jakarta.servlet.http.HttpServletRequest request) {
        String header = request.getHeader(CorrelationIds.USER_HEADER);
        if (header == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return UUID.fromString(header);
    }
}
