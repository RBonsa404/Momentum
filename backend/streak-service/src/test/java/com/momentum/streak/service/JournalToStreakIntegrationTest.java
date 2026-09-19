package com.momentum.streak.service;

import com.momentum.common.event.DomainEvent;
import com.momentum.common.event.EventTypes;
import com.momentum.common.event.payload.JournalLifecyclePayload;
import com.momentum.streak.domain.Streak;
import com.momentum.streak.domain.StreakRule;
import com.momentum.streak.repository.StreakRepository;
import com.momentum.streak.repository.StreakRuleRepository;
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
class JournalToStreakIntegrationTest {

    @Mock
    private StreakRepository streaks;

    @Mock
    private StreakRuleRepository rules;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private StreakApplicationService service;

    @Test
    void onJournalSubmitted_incrementsStreak() {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        
        Streak streak = new Streak();
        streak.setId(UUID.randomUUID());
        streak.setUserId(userId);
        streak.setCurrentLength(5);
        streak.setLongestLength(10);
        streak.setLastQualifiedDate(date.minusDays(1));
        streak.setStatus("ACTIVE");
        
        StreakRule rule = new StreakRule();
        rule.setId(UUID.randomUUID());
        rule.setUserId(userId);
        rule.setOffWeekdays("");
        rule.setJokersPerMonth(2);
        rule.setJokersRemaining(2);
        
        when(streaks.findByUserId(userId)).thenReturn(Optional.of(streak));
        when(rules.findByUserId(userId)).thenReturn(Optional.of(rule));
        when(streaks.save(any(Streak.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(rules.save(any(StreakRule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        JournalLifecyclePayload payload = new JournalLifecyclePayload();
        payload.setDayDate(date);
        payload.setMissed(false);
        
        DomainEvent<JournalLifecyclePayload> event = new DomainEvent<>(
                EventTypes.JOURNAL_SUBMITTED, "correlation-123", userId, payload);
        
        service.onJournal(event);
        
        assertThat(streak.getCurrentLength()).isEqualTo(6);
        assertThat(streak.getLongestLength()).isEqualTo(10);
        assertThat(streak.getLastQualifiedDate()).isEqualTo(date);
        assertThat(streak.getStatus()).isEqualTo("ACTIVE");
        
        verify(rabbitTemplate).convertAndSend(eq(EventTypes.EXCHANGE), eq(EventTypes.STREAK_UPDATED), any(DomainEvent.class));
    }

    @Test
    void onJournalMissed_breaksStreak_whenNoJokers() {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        
        Streak streak = new Streak();
        streak.setId(UUID.randomUUID());
        streak.setUserId(userId);
        streak.setCurrentLength(7);
        streak.setLongestLength(15);
        streak.setLastQualifiedDate(date.minusDays(1));
        streak.setStatus("ACTIVE");
        
        StreakRule rule = new StreakRule();
        rule.setId(UUID.randomUUID());
        rule.setUserId(userId);
        rule.setOffWeekdays("");
        rule.setJokersPerMonth(2);
        rule.setJokersRemaining(0);
        
        when(streaks.findByUserId(userId)).thenReturn(Optional.of(streak));
        when(rules.findByUserId(userId)).thenReturn(Optional.of(rule));
        when(streaks.save(any(Streak.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(rules.save(any(StreakRule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        JournalLifecyclePayload payload = new JournalLifecyclePayload();
        payload.setDayDate(date);
        payload.setMissed(true);
        
        DomainEvent<JournalLifecyclePayload> event = new DomainEvent<>(
                EventTypes.JOURNAL_MISSED, "correlation-456", userId, payload);
        
        service.onJournal(event);
        
        assertThat(streak.getCurrentLength()).isEqualTo(0);
        assertThat(streak.getStatus()).isEqualTo("BROKEN");
        
        verify(rabbitTemplate).convertAndSend(eq(EventTypes.EXCHANGE), eq(EventTypes.STREAK_UPDATED), any(DomainEvent.class));
    }

    @Test
    void onJournalMissed_usesJoker_whenAvailable() {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        
        Streak streak = new Streak();
        streak.setId(UUID.randomUUID());
        streak.setUserId(userId);
        streak.setCurrentLength(5);
        streak.setLongestLength(10);
        streak.setLastQualifiedDate(date.minusDays(1));
        streak.setStatus("ACTIVE");
        
        StreakRule rule = new StreakRule();
        rule.setId(UUID.randomUUID());
        rule.setUserId(userId);
        rule.setOffWeekdays("");
        rule.setJokersPerMonth(2);
        rule.setJokersRemaining(1);
        
        when(streaks.findByUserId(userId)).thenReturn(Optional.of(streak));
        when(rules.findByUserId(userId)).thenReturn(Optional.of(rule));
        when(streaks.save(any(Streak.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(rules.save(any(StreakRule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        JournalLifecyclePayload payload = new JournalLifecyclePayload();
        payload.setDayDate(date);
        payload.setMissed(true);
        
        DomainEvent<JournalLifecyclePayload> event = new DomainEvent<>(
                EventTypes.JOURNAL_MISSED, "correlation-789", userId, payload);
        
        service.onJournal(event);
        
        assertThat(streak.getCurrentLength()).isEqualTo(5);
        assertThat(rule.getJokersRemaining()).isEqualTo(0);
        assertThat(streak.getStatus()).isEqualTo("JOKER");
        assertThat(streak.getLastQualifiedDate()).isEqualTo(date);
        
        verify(rabbitTemplate).convertAndSend(eq(EventTypes.EXCHANGE), eq(EventTypes.STREAK_UPDATED), any(DomainEvent.class));
    }

    @Test
    void onJournalMissed_skipsOnOffDay() {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2026, 9, 13); // Saturday
        
        Streak streak = new Streak();
        streak.setId(UUID.randomUUID());
        streak.setUserId(userId);
        streak.setCurrentLength(5);
        streak.setLongestLength(10);
        streak.setLastQualifiedDate(date.minusDays(1));
        streak.setStatus("ACTIVE");
        
        StreakRule rule = new StreakRule();
        rule.setId(UUID.randomUUID());
        rule.setUserId(userId);
        rule.setOffWeekdays("SATURDAY,SUNDAY");
        rule.setJokersPerMonth(2);
        rule.setJokersRemaining(1);
        
        when(streaks.findByUserId(userId)).thenReturn(Optional.of(streak));
        when(rules.findByUserId(userId)).thenReturn(Optional.of(rule));
        when(streaks.save(any(Streak.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(rules.save(any(StreakRule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        JournalLifecyclePayload payload = new JournalLifecyclePayload();
        payload.setDayDate(date);
        payload.setMissed(true);
        
        DomainEvent<JournalLifecyclePayload> event = new DomainEvent<>(
                EventTypes.JOURNAL_MISSED, "correlation-999", userId, payload);
        
        service.onJournal(event);
        
        assertThat(streak.getCurrentLength()).isEqualTo(5);
        assertThat(rule.getJokersRemaining()).isEqualTo(1);
        assertThat(streak.getStatus()).isEqualTo("OFF_DAY");
        assertThat(streak.getLastQualifiedDate()).isEqualTo(date);
        
        verify(rabbitTemplate).convertAndSend(eq(EventTypes.EXCHANGE), eq(EventTypes.STREAK_UPDATED), any(DomainEvent.class));
    }
}