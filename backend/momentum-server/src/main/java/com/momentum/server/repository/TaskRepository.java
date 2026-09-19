package com.momentum.server.repository;

import com.momentum.server.domain.planning.Task;
import com.momentum.server.domain.planning.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByDayId(UUID dayId);
    List<Task> findByDayIdAndStatus(UUID dayId, TaskStatus status);
}
