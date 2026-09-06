package com.momentum.planning.service;

import com.momentum.planning.domain.RecurrenceType;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class RecurrenceServiceTest {
    private final RecurrenceService service = new RecurrenceService();

    @Test
    void dailyMovesToNextDay() {
        assertThat(service.nextOccurrence(RecurrenceType.DAILY, LocalDate.of(2026, 9, 6)))
                .isEqualTo(LocalDate.of(2026, 9, 7));
    }

    @Test
    void weekdaysSkipWeekend() {
        LocalDate friday = LocalDate.of(2026, 9, 4);
        assertThat(friday.getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
        assertThat(service.nextOccurrence(RecurrenceType.WEEKDAYS, friday))
                .isEqualTo(LocalDate.of(2026, 9, 7));
    }

    @Test
    void weeklyKeepsWeekday() {
        LocalDate sunday = LocalDate.of(2026, 9, 6);
        assertThat(service.nextOccurrence(RecurrenceType.WEEKLY, sunday))
                .isEqualTo(LocalDate.of(2026, 9, 13));
    }

    @Test
    void noneHasNoNext() {
        assertThat(service.nextOccurrence(RecurrenceType.NONE, LocalDate.now())).isNull();
    }
}
