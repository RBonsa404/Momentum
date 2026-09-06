package com.momentum.notification.controller;

import com.momentum.common.tracing.CorrelationIds;
import com.momentum.notification.domain.Notification;
import com.momentum.notification.repository.NotificationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationRepository notifications;

    public NotificationController(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @GetMapping
    public List<Notification> list(HttpServletRequest request) {
        String header = request.getHeader(CorrelationIds.USER_HEADER);
        if (header == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return notifications.findByUserIdOrderByCreatedAtDesc(UUID.fromString(header));
    }
}
