package com.momentum.planning.service;

import com.momentum.planning.domain.Day;
import com.momentum.planning.domain.TaskStatus;
import com.momentum.planning.messaging.PlanningEventPublisher;
import com.momentum.planning.repository.DayRepository;
import com.momentum.planning.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class DayCloseScheduler {
    private static final Logger log = LoggerFactory.getLogger(DayCloseScheduler.class);
    
    private final DayRepository dayRepository;
    private final TaskRepository taskRepository;
    private final CarryOverService carryOverService;
    private final PlanningEventPublisher publisher;

    public DayCloseScheduler(
            DayRepository dayRepository,
            TaskRepository taskRepository,
            CarryOverService carryOverService,
            PlanningEventPublisher publisher) {
        this.dayRepository = dayRepository;
        this.taskRepository = taskRepository;
        this.carryOverService = carryOverService;
        this.publisher = publisher;
    }

    @Scheduled(cron = "0 0 23 * * ?", zone = "UTC")
    @Transactional
    public void closeDaysForAllUsers() {
        LocalDate today = LocalDate.now();
        log.info("Starting automatic day close for date: {}", today);
        
        List<Day> openDays = dayRepository.findByDateAndClosedFalse(today);
        log.info("Found {} open days to close", openDays.size());
        
        for (Day day : openDays) {
            closeDayForUser(day);
        }
        
        log.info("Completed automatic day close for {} days", openDays.size());
    }

    private void closeDayForUser(Day day) {
        if (day.isClosed()) {
            return;
        }
        
        UUID userId = day.getUserId();
        String correlationId = UUID.randomUUID().toString();
        
        day.setClosed(true);
        dayRepository.save(day);
        
        Day next = getOrCreateDay(userId, day.getDate().plusDays(1));
        var unfinished = taskRepository.findByDayIdAndStatus(day.getId(), TaskStatus.TODO);
        var postponed = carryOverService.postponeUnfinished(unfinished, next.getId());
        
        for (var copy : postponed) {
            var saved = taskRepository.save(copy);
            publisher.taskPostponed(userId, correlationId, saved, next);
        }
        
        publisher.dayClosed(userId, correlationId, day);
        log.info("Closed day {} for user {}", day.getDate(), userId);
    }

    private Day getOrCreateDay(UUID userId, LocalDate date) {
        return dayRepository.findByUserIdAndDate(userId, date).orElseGet(() -> {
            Day day = new Day();
            day.setUserId(userId);
            day.setDate(date);
            day.setClosed(false);
            return dayRepository.save(day);
        });
    }
}