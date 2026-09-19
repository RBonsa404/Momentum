package com.momentum.server.repository;

import com.momentum.server.domain.journal.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, UUID> {
    Optional<JournalEntry> findByUserIdAndDayDate(UUID userId, LocalDate dayDate);
    List<JournalEntry> findByUserIdOrderByDayDateDesc(UUID userId);
    boolean existsByUserIdAndDayDate(UUID userId, LocalDate dayDate);
}
