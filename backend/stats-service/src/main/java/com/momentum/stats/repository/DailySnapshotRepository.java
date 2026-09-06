package com.momentum.stats.repository;

import com.momentum.stats.domain.DailySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DailySnapshotRepository extends JpaRepository<DailySnapshot, UUID> {
    Optional<DailySnapshot> findByUserIdAndDayDate(UUID userId, LocalDate dayDate);
    List<DailySnapshot> findByUserIdAndDayDateBetweenOrderByDayDateAsc(UUID userId, LocalDate from, LocalDate to);
}
