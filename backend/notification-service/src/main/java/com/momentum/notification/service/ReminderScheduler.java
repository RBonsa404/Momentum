package com.momentum.notification.service;

import com.momentum.common.event.EventTypes;
import com.momentum.notification.domain.Notification;
import com.momentum.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ReminderScheduler {
    private static final Logger log = LoggerFactory.getLogger(ReminderScheduler.class);
    private final NotificationRepository notifications;

    public ReminderScheduler(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @Scheduled(cron = "0 0 21 * * ?", zone = "UTC")
    public void eveningReminder() {
        log.info("Evening reminder tick {}", LocalDate.now());
        log.info("TODO: Send evening reminders to all users about incomplete tasks and journal");
    }

    @Scheduled(cron = "0 0 8 * * ?", zone = "UTC")
    public void morningDigest() {
        log.info("Morning digest tick {}", LocalDate.now());
        log.info("TODO: Send morning digest with today's schedule and goals");
    }

    @Scheduled(cron = "0 */30 * * * ?", zone = "UTC")
    public void checkUpcomingBlocks() {
        log.info("Checking upcoming blocks at {}", LocalTime.now());
        log.info("TODO: Check for upcoming time blocks and send reminders");
    }
}