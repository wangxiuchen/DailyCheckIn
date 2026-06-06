package com.example.dailycheckin.viewmodel

import java.time.LocalDate

fun calculateCurrentStreak(
    recordDates: Collection<String>,
    today: LocalDate,
): Int {
    val dates = recordDates.mapNotNull { date ->
        runCatching { LocalDate.parse(date) }.getOrNull()
    }.toSet()

    var streak = 0
    var dateToCheck = today
    while (dateToCheck in dates) {
        streak += 1
        dateToCheck = dateToCheck.minusDays(1)
    }
    return streak
}

