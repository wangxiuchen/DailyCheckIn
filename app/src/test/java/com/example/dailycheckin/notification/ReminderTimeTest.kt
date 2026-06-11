package com.example.dailycheckin.notification

import java.time.Duration
import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class ReminderTimeTest {

    @Test
    fun `delay until later today when target hour is ahead`() {
        val now = LocalDateTime.of(2026, 6, 6, 9, 0)
        val delay = millisUntilNextHour(10, now)

        assertEquals(Duration.ofHours(1).toMillis(), delay)
    }

    @Test
    fun `rolls over to tomorrow when target hour already passed`() {
        val now = LocalDateTime.of(2026, 6, 6, 11, 0)
        val delay = millisUntilNextHour(10, now)

        assertEquals(Duration.ofHours(23).toMillis(), delay)
    }

    @Test
    fun `rolls over to tomorrow when exactly at target hour`() {
        val now = LocalDateTime.of(2026, 6, 6, 10, 0)
        val delay = millisUntilNextHour(10, now)

        assertEquals(Duration.ofHours(24).toMillis(), delay)
    }

    @Test
    fun `evening reminder delay is measured correctly`() {
        val now = LocalDateTime.of(2026, 6, 6, 21, 30)
        val delay = millisUntilNextHour(22, now)

        assertEquals(Duration.ofMinutes(30).toMillis(), delay)
    }
}
