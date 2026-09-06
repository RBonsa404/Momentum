package com.momentum.planning.repository;

import com.momentum.planning.domain.Task;
import com.momentum.planning.domain.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByDayId(UUID dayId);
    List<Task> findByDayIdAndStatus(UUID dayId, TaskStatus status);
}
