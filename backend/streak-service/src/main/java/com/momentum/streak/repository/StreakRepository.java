package com.momentum.streak.repository;

import com.momentum.streak.domain.Streak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StreakRepository extends JpaRepository<Streak, UUID> {
    Optional<Streak> findByUserId(UUID userId);
}
