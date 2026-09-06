package com.momentum.planning.repository;

import com.momentum.planning.domain.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface DayRepository extends JpaRepository<Day, UUID> {
    Optional<Day> findByUserIdAndDate(UUID userId, LocalDate date);
}
