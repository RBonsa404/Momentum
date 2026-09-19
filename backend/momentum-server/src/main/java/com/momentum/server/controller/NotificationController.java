package com.momentum.server.controller;

import com.momentum.server.config.SecurityUtils;
import com.momentum.server.domain.notification.Notification;
import com.momentum.server.dto.notification.NotificationDto;
import com.momentum.server.repository.NotificationRepository;
import com.momentum.server.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationRepository notifications;
    private final NotificationService notificationService;

    public NotificationController(NotificationRepository notifications, NotificationService notificationService) {
        this.notifications = notifications;
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationDto> list() {
        return notifications.findByUserIdOrderByCreatedAtDesc(SecurityUtils.getUserId())
                .stream()
                .map(notificationService::toDto)
                .toList();
    }

    @PatchMapping("/{id}/read")
    public NotificationDto markAsRead(@PathVariable UUID id) {
        Notification notification = notifications.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        notification.setReadFlag(true);
        return notificationService.toDto(notifications.save(notification));
    }
}
