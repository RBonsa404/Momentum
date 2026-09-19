package com.momentum.server.repository;

import com.momentum.server.domain.planning.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubTaskRepository extends JpaRepository<SubTask, UUID> {
    List<SubTask> findByTaskIdOrderBySortOrderAsc(UUID taskId);
}
