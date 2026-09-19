package com.momentum.goal.mapper;

import com.momentum.goal.domain.Goal;
import com.momentum.goal.domain.Habit;
import com.momentum.goal.domain.Milestone;
import com.momentum.goal.dto.GoalDto;
import com.momentum.goal.dto.HabitDto;
import com.momentum.goal.dto.MilestoneDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoalMapper {
    GoalDto toDto(Goal goal);
    MilestoneDto toDto(Milestone milestone);
    HabitDto toDto(Habit habit);
}