package com.momentum.notification.controller;

import com.momentum.common.tracing.CorrelationIds;
import com.momentum.notification.domain.Notification;
import com.momentum.notification.dto.NotificationDto;
import com.momentum.notification.mapper.NotificationMapper;
import com.momentum.notification.repository.NotificationRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    private final NotificationMapper mapper;

    public NotificationController(NotificationRepository notifications, NotificationMapper mapper) {
        this.notifications = notifications;
        this.mapper = mapper;
    }

    @GetMapping
    public List<NotificationDto> list(HttpServletRequest request) {
        String header = request.getHeader(CorrelationIds.USER_HEADER);
        if (header == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return notifications.findByUserIdOrderByCreatedAtDesc(UUID.fromString(header))
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @PatchMapping("/{id}/read")
    public NotificationDto markAsRead(@PathVariable UUID id) {
        Notification notification = notifications.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        notification.setReadFlag(true);
        return mapper.toDto(notifications.save(notification));
    }
}
