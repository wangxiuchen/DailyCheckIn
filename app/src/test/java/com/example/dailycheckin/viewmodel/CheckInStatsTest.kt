package com.example.dailycheckin.viewmodel

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckInStatsTest {
    private val today = LocalDate.of(2026, 6, 6)

    @Test
    fun `returns zero current streak when today is missing`() {
        val result = calculateStatistics(
            recordDates = listOf("2026-06-05", "2026-06-04"),
            today = today,
        )

        assertEquals(0, result.currentStreak)
        assertEquals(2, result.longestStreak)
    }

    @Test
    fun `calculates current and longest streak independently`() {
        val result = calculateStatistics(
            recordDates = listOf(
                "2026-06-06",
                "2026-06-05",
                "2026-05-20",
                "2026-05-19",
                "2026-05-18",
            ),
            today = today,
        )

        assertEquals(2, result.currentStreak)
        assertEquals(3, result.longestStreak)
        assertEquals(5, result.totalDays)
    }

    @Test
    fun `counts consecutive days from today`() {
        val result = calculateStatistics(
            recordDates = listOf("2026-06-06", "2026-06-05", "2026-06-04"),
            today = today,
        )

        assertEquals(3, result.currentStreak)
        assertEquals(3, result.longestStreak)
    }

    @Test
    fun `stops at first missing day`() {
        val result = calculateStatistics(
            recordDates = listOf("2026-06-06", "2026-06-04"),
            today = today,
        )

        assertEquals(1, result.currentStreak)
    }

    @Test
    fun `calculates current month rate through today`() {
        val result = calculateStatistics(
            recordDates = listOf("2026-06-06", "2026-06-02", "2026-06-01"),
            today = today,
        )

        assertEquals(3, result.currentMonthDays)
        assertEquals(50, result.currentMonthRate)
    }

    @Test
    fun `ignores invalid duplicate and future dates`() {
        val result = calculateStatistics(
            recordDates = listOf(
                "2026-06-06",
                "2026-06-06",
                "2026-06-07",
                "not-a-date",
            ),
            today = today,
        )

        assertEquals(1, result.totalDays)
        assertEquals(1, result.currentStreak)
    }
}
