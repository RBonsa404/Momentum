package com.momentum.planning.service;

import com.momentum.planning.domain.RecurrenceType;
import com.momentum.planning.domain.Task;
import com.momentum.planning.domain.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CarryOverServiceTest {
    private final CarryOverService service = new CarryOverService();

    @Test
    void postponesOnlyTodoTasksAndIncrementsCounter() {
        Task done = task(TaskStatus.DONE, 0);
        Task todo = task(TaskStatus.TODO, 2);
        UUID nextDay = UUID.randomUUID();

        List<Task> result = service.postponeUnfinished(List.of(done, todo), nextDay);

        assertThat(result).hasSize(1);
        Task copy = result.get(0);
        assertThat(copy.getDayId()).isEqualTo(nextDay);
        assertThat(copy.getTitle()).isEqualTo(todo.getTitle());
        assertThat(copy.getPostponedCount()).isEqualTo(3);
        assertThat(copy.getOriginTaskId()).isEqualTo(todo.getId());
        assertThat(copy.getTimeBlockId()).isNull();
    }

    @Test
    void keepsOriginAcrossRepeatedPostpones() {
        UUID origin = UUID.randomUUID();
        Task alreadyPostponed = task(TaskStatus.TODO, 1);
        alreadyPostponed.setOriginTaskId(origin);

        List<Task> result = service.postponeUnfinished(List.of(alreadyPostponed), UUID.randomUUID());

        assertThat(result.get(0).getOriginTaskId()).isEqualTo(origin);
    }

    private Task task(TaskStatus status, int postponed) {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDayId(UUID.randomUUID());
        task.setTitle("Deep work");
        task.setNotes("notes");
        task.setStatus(status);
        task.setRecurrence(RecurrenceType.NONE);
        task.setPostponedCount(postponed);
        return task;
    }
}
