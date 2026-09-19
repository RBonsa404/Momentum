package com.momentum.server.service;

import com.momentum.server.domain.notification.Notification;
import com.momentum.server.dto.notification.NotificationDto;
import com.momentum.server.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notifications;

    public NotificationService(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    public void onJournalMissed(UUID userId) {
        push(userId, "Récap manquant", "Ton journal du soir n'a pas été rédigé.");
    }

    public void onDayClosed(UUID userId) {
        push(userId, "Journée close", "Les tâches non finies ont été reportées.");
    }

    public void push(UUID userId, String title, String body) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setBody(body);
        notifications.save(notification);
        log.info("notify user={} title={}", userId, title);
    }

    public NotificationDto toDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setTitle(notification.getTitle());
        dto.setBody(notification.getBody());
        dto.setReadFlag(notification.isReadFlag());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
