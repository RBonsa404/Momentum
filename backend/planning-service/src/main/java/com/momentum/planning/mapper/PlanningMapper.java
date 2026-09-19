package com.momentum.planning.mapper;

import com.momentum.planning.domain.Day;
import com.momentum.planning.domain.SubTask;
import com.momentum.planning.domain.Task;
import com.momentum.planning.domain.TimeBlock;
import com.momentum.planning.dto.DayDto;
import com.momentum.planning.dto.SubTaskDto;
import com.momentum.planning.dto.TaskDto;
import com.momentum.planning.dto.TimeBlockDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlanningMapper {
    DayDto toDto(Day day);
    TimeBlockDto toDto(TimeBlock block);
    TaskDto toDto(Task task);
    SubTaskDto toDto(SubTask subTask);
}