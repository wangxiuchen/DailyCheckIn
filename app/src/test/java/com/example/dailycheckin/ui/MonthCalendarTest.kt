package com.example.dailycheckin.ui

import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MonthCalendarTest {
    @Test
    fun `starts monday based month without leading blanks`() {
        val dates = calendarDates(YearMonth.of(2026, 6))

        assertEquals(LocalDate.of(2026, 6, 1), dates.first())
        assertEquals(35, dates.size)
    }

    @Test
    fun `adds leading and trailing blanks to complete weeks`() {
        val dates = calendarDates(YearMonth.of(2026, 8))

        assertNull(dates.first())
        assertEquals(LocalDate.of(2026, 8, 1), dates[5])
        assertEquals(42, dates.size)
    }
}
