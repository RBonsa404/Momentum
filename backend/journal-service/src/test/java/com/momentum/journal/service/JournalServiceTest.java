package com.momentum.journal.service;

import com.momentum.common.event.DomainEvent;
import com.momentum.common.event.EventTypes;
import com.momentum.journal.domain.JournalEntry;
import com.momentum.journal.domain.TrackedUser;
import com.momentum.journal.dto.SubmitJournalRequest;
import com.momentum.journal.repository.JournalEntryRepository;
import com.momentum.journal.repository.TrackedUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JournalServiceTest {

    @Mock
    private JournalEntryRepository entries;

    @Mock
    private TrackedUserRepository trackedUsers;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private JournalService service;

    @Test
    void submit_createsNewEntry_whenNotExists() {
        UUID userId = UUID.randomUUID();
        SubmitJournalRequest request = new SubmitJournalRequest();
        request.setWins("Great day");
        request.setMood(8);
        request.setEnergy(7);

        when(entries.findByUserIdAndDayDate(userId, LocalDate.now())).thenReturn(Optional.empty());
        when(trackedUsers.existsById(userId)).thenReturn(false);
        when(entries.save(any(JournalEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JournalEntry result = service.submit(userId, "correlation-123", request);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getWins()).isEqualTo("Great day");
        assertThat(result.getMood()).isEqualTo(8);
        assertThat(result.getEnergy()).isEqualTo(7);
        verify(trackedUsers).save(any(TrackedUser.class));
        verify(rabbitTemplate).convertAndSend(eq(EventTypes.EXCHANGE), eq(EventTypes.JOURNAL_SUBMITTED), any(DomainEvent.class));
    }

    @Test
    void submit_updatesExistingEntry_whenExists() {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        SubmitJournalRequest request = new SubmitJournalRequest();
        request.setDayDate(date);
        request.setWins("Updated wins");
        request.setMood(9);
        request.setEnergy(6);

        JournalEntry existing = new JournalEntry();
        existing.setId(UUID.randomUUID());
        existing.setUserId(userId);
        existing.setDayDate(date);
        existing.setWins("Old wins");

        when(entries.findByUserIdAndDayDate(userId, date)).thenReturn(Optional.of(existing));
        when(entries.save(any(JournalEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JournalEntry result = service.submit(userId, "correlation-456", request);

        assertThat(result.getWins()).isEqualTo("Updated wins");
        assertThat(result.getMood()).isEqualTo(9);
        verify(entries).save(existing);
    }

    @Test
    void track_createsTrackedUser_whenNotExists() {
        UUID userId = UUID.randomUUID();
        when(trackedUsers.existsById(userId)).thenReturn(false);
        when(trackedUsers.save(any(TrackedUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.track(userId);

        verify(trackedUsers).save(any(TrackedUser.class));
    }

    @Test
    void track_skips_whenUserAlreadyTracked() {
        UUID userId = UUID.randomUUID();
        when(trackedUsers.existsById(userId)).thenReturn(true);

        service.track(userId);

        verify(trackedUsers, never()).save(any(TrackedUser.class));
    }

    @Test
    void markMissed_publishesEvent_whenNoEntryExists() {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        when(entries.existsByUserIdAndDayDate(userId, date)).thenReturn(false);

        service.markMissed(userId, date, "correlation-789");

        ArgumentCaptor<DomainEvent<?>> captor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(rabbitTemplate).convertAndSend(eq(EventTypes.EXCHANGE), eq(EventTypes.JOURNAL_MISSED), captor.capture());

        DomainEvent<?> event = captor.getValue();
        assertThat(event.getEventType()).isEqualTo(EventTypes.JOURNAL_MISSED);
        assertThat(event.getUserId()).isEqualTo(userId);
    }

    @Test
    void markMissed_skips_whenEntryExists() {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        when(entries.existsByUserIdAndDayDate(userId, date)).thenReturn(true);

        service.markMissed(userId, date, "correlation-789");

        verify(rabbitTemplate, never()).convertAndSend(any(String.class), any(String.class), any(Object.class));
    }
}