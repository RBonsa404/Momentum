package com.momentum.server.service;

import com.momentum.server.domain.stats.DailySnapshot;
import com.momentum.server.dto.stats.DailySnapshotDto;
import com.momentum.server.repository.DailySnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class StatsService {
    private final DailySnapshotRepository snapshots;

    public StatsService(DailySnapshotRepository snapshots) {
        this.snapshots = snapshots;
    }

    @Transactional
    public void onTaskCreated(UUID userId, LocalDate date) {
        DailySnapshot snap = snapshot(userId, date);
        snap.setPlanned(snap.getPlanned() + 1);
        snap.setUnfinished(snap.getUnfinished() + 1);
        snapshots.save(snap);
    }

    @Transactional
    public void onTaskCompleted(UUID userId, LocalDate date) {
        DailySnapshot snap = snapshot(userId, date);
        snap.setDone(snap.getDone() + 1);
        snap.setUnfinished(Math.max(0, snap.getUnfinished() - 1));
        snapshots.save(snap);
    }

    @Transactional
    public void onTaskPostponed(UUID userId, LocalDate date) {
        DailySnapshot snap = snapshot(userId, date);
        snap.setPostponed(snap.getPostponed() + 1);
        snapshots.save(snap);
    }

    @Transactional
    public void onJournalSubmitted(UUID userId, LocalDate date, Integer mood, Integer energy) {
        DailySnapshot snap = snapshot(userId, date);
        snap.setSubmittedJournals(1);
        snap.setMissedJournals(0);
        snap.setMood(mood);
        snap.setEnergy(energy);
        snapshots.save(snap);
    }

    @Transactional
    public void onJournalMissed(UUID userId, LocalDate date) {
        DailySnapshot snap = snapshot(userId, date);
        snap.setMissedJournals(1);
        snapshots.save(snap);
    }

    @Transactional
    public void onStreakUpdated(UUID userId, LocalDate date, int currentLength) {
        DailySnapshot snap = snapshot(userId, date);
        snap.setStreakLength(currentLength);
        snapshots.save(snap);
    }

    public DailySnapshotDto toDto(DailySnapshot snap) {
        DailySnapshotDto dto = new DailySnapshotDto();
        dto.setId(snap.getId());
        dto.setUserId(snap.getUserId());
        dto.setDayDate(snap.getDayDate());
        dto.setPlanned(snap.getPlanned());
        dto.setDone(snap.getDone());
        dto.setUnfinished(snap.getUnfinished());
        dto.setPostponed(snap.getPostponed());
        dto.setMissedJournals(snap.getMissedJournals());
        dto.setSubmittedJournals(snap.getSubmittedJournals());
        dto.setMood(snap.getMood() == null ? null : String.valueOf(snap.getMood()));
        dto.setEnergy(snap.getEnergy() == null ? null : String.valueOf(snap.getEnergy()));
        dto.setStreakLength(snap.getStreakLength());
        return dto;
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
