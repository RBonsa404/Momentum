package com.momentum.server.repository;

import com.momentum.server.domain.planning.TimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TimeBlockRepository extends JpaRepository<TimeBlock, UUID> {
    List<TimeBlock> findByDayIdOrderByStartTimeAsc(UUID dayId);
}
