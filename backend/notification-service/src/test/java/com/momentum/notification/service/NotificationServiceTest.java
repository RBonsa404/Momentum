package com.momentum.notification.service;

import com.momentum.common.event.DomainEvent;
import com.momentum.common.event.EventTypes;
import com.momentum.notification.domain.Notification;
import com.momentum.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notifications;

    @InjectMocks
    private NotificationService service;

    @Test
    void onEvent_createsNotificationForJournalMissed() {
        UUID userId = UUID.randomUUID();
        DomainEvent<Map<String, Object>> event = new DomainEvent<>(
                EventTypes.JOURNAL_MISSED, "correlation-123", userId, Map.of());

        when(notifications.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.onEvent(event);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notifications).save(captor.capture());
        Notification notification = captor.getValue();
        assertThat(notification.getUserId()).isEqualTo(userId);
        assertThat(notification.getTitle()).isEqualTo("Récap manquant");
        assertThat(notification.getBody()).isEqualTo("Ton journal du soir n'a pas été rédigé.");
    }

    @Test
    void onEvent_createsNotificationForDayClosed() {
        UUID userId = UUID.randomUUID();
        DomainEvent<Map<String, Object>> event = new DomainEvent<>(
                EventTypes.DAY_CLOSED, "correlation-456", userId, Map.of());

        when(notifications.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.onEvent(event);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notifications).save(captor.capture());
        Notification notification = captor.getValue();
        assertThat(notification.getUserId()).isEqualTo(userId);
        assertThat(notification.getTitle()).isEqualTo("Journée close");
        assertThat(notification.getBody()).isEqualTo("Les tâches non finies ont été reportées.");
    }

    @Test
    void onEvent_skipsWhenUserIdIsNull() {
        DomainEvent<Map<String, Object>> event = new DomainEvent<>(
                EventTypes.JOURNAL_MISSED, "correlation-789", null, Map.of());

        service.onEvent(event);

        verify(notifications, never()).save(any(Notification.class));
    }

    @Test
    void push_savesNotification() {
        UUID userId = UUID.randomUUID();
        when(notifications.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.push(userId, "Test Title", "Test Body");

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notifications).save(captor.capture());
        Notification notification = captor.getValue();
        assertThat(notification.getUserId()).isEqualTo(userId);
        assertThat(notification.getTitle()).isEqualTo("Test Title");
        assertThat(notification.getBody()).isEqualTo("Test Body");
    }
}