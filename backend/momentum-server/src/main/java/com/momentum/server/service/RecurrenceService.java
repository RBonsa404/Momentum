package com.momentum.server.service;

import com.momentum.server.domain.planning.RecurrenceType;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class RecurrenceService {

    public LocalDate nextOccurrence(RecurrenceType type, LocalDate from) {
        if (type == null || type == RecurrenceType.NONE) {
            return null;
        }
        LocalDate cursor = from.plusDays(1);
        return switch (type) {
            case DAILY -> cursor;
            case WEEKLY -> from.plusWeeks(1);
            case MONTHLY -> from.plusMonths(1);
            case NONE -> null;
        };
    }

    public boolean occursOn(RecurrenceType type, LocalDate origin, LocalDate candidate) {
        if (type == RecurrenceType.NONE || candidate.isBefore(origin)) {
            return false;
        }
        return switch (type) {
            case DAILY -> true;
            case WEEKLY -> candidate.getDayOfWeek() == origin.getDayOfWeek();
            case MONTHLY -> candidate.getDayOfMonth() == origin.getDayOfMonth();
            case NONE -> false;
        };
    }
}
