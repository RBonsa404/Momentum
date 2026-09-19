package com.momentum.server.service;

import com.momentum.server.domain.planning.Task;
import com.momentum.server.domain.planning.TaskStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CarryOverService {

    public List<Task> postponeUnfinished(List<Task> sourceTasks, UUID targetDayId) {
        List<Task> postponed = new ArrayList<>();
        for (Task source : sourceTasks) {
            if (source.getStatus() != TaskStatus.TODO) {
                continue;
            }
            Task copy = new Task();
            copy.setDayId(targetDayId);
            copy.setTimeBlockId(null);
            copy.setTitle(source.getTitle());
            copy.setNotes(source.getNotes());
            copy.setStatus(TaskStatus.TODO);
            copy.setRecurrence(source.getRecurrence());
            copy.setPostponedCount(source.getPostponedCount() + 1);
            copy.setOriginTaskId(source.getOriginTaskId() == null ? source.getId() : source.getOriginTaskId());
            postponed.add(copy);
        }
        return postponed;
    }
}
