package com.momentum.notification.service;

import com.momentum.common.event.DomainEvent;
import com.momentum.common.event.EventTypes;
import com.momentum.notification.domain.Notification;
import com.momentum.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notifications;

    public NotificationService(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @RabbitListener(queues = "momentum.notification.events")
    public void onEvent(DomainEvent<Map<String, Object>> event) {
        if (event.getUserId() == null) {
            return;
        }
        if (EventTypes.JOURNAL_MISSED.equals(event.getEventType())) {
            push(event.getUserId(), "Récap manquant", "Ton journal du soir n'a pas été rédigé.");
        }
        if (EventTypes.DAY_CLOSED.equals(event.getEventType())) {
            push(event.getUserId(), "Journée close", "Les tâches non finies ont été reportées.");
        }
    }

    @Scheduled(cron = "${momentum.notification.morning-cron:0 30 7 * * *}")
    public void morningDigest() {
        log.info("Morning digest tick {}", LocalDate.now());
    }

    public void push(UUID userId, String title, String body) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setBody(body);
        notifications.save(notification);
        log.info("notify user={} title={}", userId, title);
    }
}
