package com.momentum.stats.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProcrastinationScoreCalculatorTest {
    private final ProcrastinationScoreCalculator calculator = new ProcrastinationScoreCalculator();

    @Test
    void perfectDayIsZero() {
        assertThat(calculator.score(0, 0, 0, 10)).isZero();
    }

    @Test
    void clampsAtHundred() {
        assertThat(calculator.score(50, 50, 50, 10)).isEqualTo(100);
    }

    @Test
    void formulaMatchesSpec() {
        assertThat(calculator.score(2, 1, 1, 8)).isEqualTo(50);
    }
}
