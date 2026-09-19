package com.momentum.stats.mapper;

import com.momentum.stats.domain.DailySnapshot;
import com.momentum.stats.dto.DailySnapshotDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    DailySnapshotDto toDto(DailySnapshot snapshot);
}