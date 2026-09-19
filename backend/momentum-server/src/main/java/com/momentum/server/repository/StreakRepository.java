package com.momentum.server.repository;

import com.momentum.server.domain.streak.Streak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StreakRepository extends JpaRepository<Streak, UUID> {
    Optional<Streak> findByUserId(UUID userId);
}
