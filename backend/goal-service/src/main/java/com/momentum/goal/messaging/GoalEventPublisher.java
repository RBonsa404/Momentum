package com.momentum.goal.messaging;

import com.momentum.common.event.DomainEvent;
import com.momentum.common.event.EventTypes;
import com.momentum.common.event.payload.GoalProgressPayload;
import com.momentum.goal.domain.Goal;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GoalEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public GoalEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void goalProgressUpdated(UUID userId, String correlationId, Goal goal) {
        GoalProgressPayload payload = new GoalProgressPayload();
        payload.setGoalId(goal.getId());
        payload.setProgressPercent(goal.getProgressPercent());
        DomainEvent<GoalProgressPayload> event = new DomainEvent<>(
                EventTypes.GOAL_PROGRESS, correlationId, userId, payload);
        rabbitTemplate.convertAndSend(EventTypes.EXCHANGE, EventTypes.GOAL_PROGRESS, event);
    }
}