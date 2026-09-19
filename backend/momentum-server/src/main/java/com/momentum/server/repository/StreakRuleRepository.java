package com.momentum.server.repository;

import com.momentum.server.domain.streak.StreakRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StreakRuleRepository extends JpaRepository<StreakRule, UUID> {
    Optional<StreakRule> findByUserId(UUID userId);
}
