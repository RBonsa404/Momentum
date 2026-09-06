package com.momentum.planning.messaging;

import com.momentum.common.event.DomainEvent;
import com.momentum.common.event.EventTypes;
import com.momentum.common.event.payload.TaskLifecyclePayload;
import com.momentum.common.tracing.CorrelationIds;
import com.momentum.planning.domain.Day;
import com.momentum.planning.domain.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PlanningEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public PlanningEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void taskCreated(UUID userId, String correlationId, Task task, Day day) {
        publish(EventTypes.TASK_CREATED, userId, correlationId, payload(task, day, false));
    }

    public void taskCompleted(UUID userId, String correlationId, Task task, Day day) {
        publish(EventTypes.TASK_COMPLETED, userId, correlationId, payload(task, day, false));
    }

    public void taskPostponed(UUID userId, String correlationId, Task task, Day day) {
        publish(EventTypes.TASK_POSTPONED, userId, correlationId, payload(task, day, true));
    }

    public void dayClosed(UUID userId, String correlationId, Day day) {
        TaskLifecyclePayload payload = new TaskLifecyclePayload();
        payload.setDayId(day.getId());
        payload.setDayDate(day.getDate());
        publish(EventTypes.DAY_CLOSED, userId, correlationId, payload);
    }

    private TaskLifecyclePayload payload(Task task, Day day, boolean unfinished) {
        TaskLifecyclePayload payload = new TaskLifecyclePayload();
        payload.setTaskId(task.getId());
        payload.setDayId(day.getId());
        payload.setDayDate(day.getDate());
        payload.setTitle(task.getTitle());
        payload.setStatus(task.getStatus().name());
        payload.setPostponedCount(task.getPostponedCount());
        payload.setDueUnfinished(unfinished);
        return payload;
    }

    private void publish(String type, UUID userId, String correlationId, TaskLifecyclePayload payload) {
        DomainEvent<TaskLifecyclePayload> event = new DomainEvent<>(type, correlationId, userId, payload);
        rabbitTemplate.convertAndSend(EventTypes.EXCHANGE, type, event);
    }

    public static String correlationOrNew(String incoming) {
        return incoming == null || incoming.isBlank() ? UUID.randomUUID().toString() : incoming;
    }

    public static final String CORRELATION_HEADER = CorrelationIds.HTTP_HEADER;
}
