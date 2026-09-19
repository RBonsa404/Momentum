package com.momentum.stats.service;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.momentum.common.event.DomainEvent;
import com.momentum.common.event.EventTypes;
import com.momentum.common.event.payload.GoalProgressPayload;
import com.momentum.common.event.payload.JournalLifecyclePayload;
import com.momentum.common.event.payload.StreakUpdatedPayload;
import com.momentum.common.event.payload.TaskLifecyclePayload;
import com.momentum.stats.domain.DailySnapshot;
import com.momentum.stats.repository.DailySnapshotRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
public class StatsProjectionService {
    private final DailySnapshotRepository snapshots;
    private final JsonMapper jsonMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    public StatsProjectionService(DailySnapshotRepository snapshots) {
        this.snapshots = snapshots;
    }

    @RabbitListener(queues = "momentum.stats.events")
    @Transactional
    public void onEvent(DomainEvent<Map<String, Object>> event) {
        if (event.getUserId() == null || event.getEventType() == null) {
            return;
        }
        if (EventTypes.TASK_CREATED.equals(event.getEventType())
                || EventTypes.TASK_COMPLETED.equals(event.getEventType())
                || EventTypes.TASK_POSTPONED.equals(event.getEventType())
                || EventTypes.DAY_CLOSED.equals(event.getEventType())) {
            TaskLifecyclePayload payload = jsonMapper.convertValue(event.getPayload(), TaskLifecyclePayload.class);
            DailySnapshot snap = snapshot(event.getUserId(), payload.getDayDate());
            if (EventTypes.TASK_CREATED.equals(event.getEventType())) {
                snap.setPlanned(snap.getPlanned() + 1);
                snap.setUnfinished(snap.getUnfinished() + 1);
            } else if (EventTypes.TASK_COMPLETED.equals(event.getEventType())) {
                snap.setDone(snap.getDone() + 1);
                snap.setUnfinished(Math.max(0, snap.getUnfinished() - 1));
            } else if (EventTypes.TASK_POSTPONED.equals(event.getEventType())) {
                snap.setPostponed(snap.getPostponed() + 1);
            }
            snapshots.save(snap);
            return;
        }
        if (EventTypes.JOURNAL_SUBMITTED.equals(event.getEventType())
                || EventTypes.JOURNAL_MISSED.equals(event.getEventType())) {
            JournalLifecyclePayload payload = jsonMapper.convertValue(event.getPayload(), JournalLifecyclePayload.class);
            DailySnapshot snap = snapshot(event.getUserId(), payload.getDayDate());
            if (payload.isMissed() || EventTypes.JOURNAL_MISSED.equals(event.getEventType())) {
                snap.setMissedJournals(1);
            } else {
                snap.setSubmittedJournals(1);
                snap.setMissedJournals(0);
                snap.setMood(payload.getMood());
                snap.setEnergy(payload.getEnergy());
            }
            snapshots.save(snap);
            return;
        }
        if (EventTypes.STREAK_UPDATED.equals(event.getEventType())) {
            StreakUpdatedPayload payload = jsonMapper.convertValue(event.getPayload(), StreakUpdatedPayload.class);
            LocalDate date = payload.getLastQualifiedDate() == null ? LocalDate.now() : payload.getLastQualifiedDate();
            DailySnapshot snap = snapshot(event.getUserId(), date);
            snap.setStreakLength(payload.getCurrentLength());
            snapshots.save(snap);
            return;
        }
        if (EventTypes.GOAL_PROGRESS.equals(event.getEventType())) {
            GoalProgressPayload payload = jsonMapper.convertValue(event.getPayload(), GoalProgressPayload.class);
            LocalDate date = LocalDate.now();
            DailySnapshot snap = snapshot(event.getUserId(), date);
            snapshots.save(snap);
        }
    }

    private DailySnapshot snapshot(UUID userId, LocalDate date) {
        LocalDate day = date == null ? LocalDate.now() : date;
        return snapshots.findByUserIdAndDayDate(userId, day).orElseGet(() -> {
            DailySnapshot snap = new DailySnapshot();
            snap.setUserId(userId);
            snap.setDayDate(day);
            return snap;
        });
    }
}
