package com.example.dailycheckin.viewmodel

import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.roundToInt

data class CheckInStatistics(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalDays: Int = 0,
    val currentMonthDays: Int = 0,
    val currentMonthRate: Int = 0,
)

fun calculateStatistics(
    recordDates: Collection<String>,
    today: LocalDate,
): CheckInStatistics {
    val dates = recordDates.mapNotNull { date ->
        runCatching { LocalDate.parse(date) }.getOrNull()
    }.filter { !it.isAfter(today) }.toSet()

    var currentStreak = 0
    var dateToCheck = today
    while (dateToCheck in dates) {
        currentStreak += 1
        dateToCheck = dateToCheck.minusDays(1)
    }

    var longestStreak = 0
    var runningStreak = 0
    var previousDate: LocalDate? = null
    dates.sorted().forEach { date ->
        runningStreak = if (previousDate?.plusDays(1) == date) {
            runningStreak + 1
        } else {
            1
        }
        longestStreak = maxOf(longestStreak, runningStreak)
        previousDate = date
    }

    val currentMonth = YearMonth.from(today)
    val currentMonthDays = dates.count { YearMonth.from(it) == currentMonth }
    val currentMonthRate =
        (currentMonthDays * 100.0 / today.dayOfMonth).roundToInt()

    return CheckInStatistics(
        currentStreak = currentStreak,
        longestStreak = longestStreak,
        totalDays = dates.size,
        currentMonthDays = currentMonthDays,
        currentMonthRate = currentMonthRate,
    )
}
