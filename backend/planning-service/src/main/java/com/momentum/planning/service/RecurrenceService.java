package com.momentum.planning.service;

import com.momentum.planning.domain.RecurrenceType;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class RecurrenceService {

    public LocalDate nextOccurrence(RecurrenceType type, LocalDate from) {
        if (type == null || type == RecurrenceType.NONE) {
            return null;
        }
        LocalDate cursor = from.plusDays(1);
        return switch (type) {
            case DAILY -> cursor;
            case WEEKDAYS -> {
                while (cursor.getDayOfWeek() == DayOfWeek.SATURDAY || cursor.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    cursor = cursor.plusDays(1);
                }
                yield cursor;
            }
            case WEEKLY -> from.plusWeeks(1);
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
            case WEEKDAYS -> candidate.getDayOfWeek().getValue() <= 5;
            case NONE -> false;
        };
    }
}
