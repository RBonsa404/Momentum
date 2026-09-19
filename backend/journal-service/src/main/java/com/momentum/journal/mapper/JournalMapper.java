package com.momentum.journal.mapper;

import com.momentum.journal.domain.JournalEntry;
import com.momentum.journal.dto.JournalEntryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JournalMapper {
    JournalEntryDto toDto(JournalEntry entry);
}