package com.example.dailycheckin.viewmodel

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckInStatsTest {
    private val today = LocalDate.of(2026, 6, 6)

    @Test
    fun `returns zero when today is missing`() {
        val result = calculateCurrentStreak(
            recordDates = listOf("2026-06-05", "2026-06-04"),
            today = today,
        )

        assertEquals(0, result)
    }

    @Test
    fun `counts consecutive days from today`() {
        val result = calculateCurrentStreak(
            recordDates = listOf("2026-06-06", "2026-06-05", "2026-06-04"),
            today = today,
        )

        assertEquals(3, result)
    }

    @Test
    fun `stops at first missing day`() {
        val result = calculateCurrentStreak(
            recordDates = listOf("2026-06-06", "2026-06-04"),
            today = today,
        )

        assertEquals(1, result)
    }
}
