package com.example.dailycheckin.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth

private val weekLabels = listOf("一", "二", "三", "四", "五", "六", "日")

@Composable
fun MonthCalendar(
    month: YearMonth,
    checkedDates: Set<LocalDate>,
    today: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onPreviousMonth) {
                Text("上个月")
            }
            Text(
                text = "${month.year} 年 ${month.monthValue} 月",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            TextButton(onClick = onNextMonth) {
                Text("下个月")
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            weekLabels.forEach { label ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        calendarDates(month).chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (date != null) {
                            CalendarDay(
                                date = date,
                                isChecked = date in checkedDates,
                                isToday = date == today,
                                isFuture = date.isAfter(today),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate,
    isChecked: Boolean,
    isToday: Boolean,
    isFuture: Boolean,
) {
    val backgroundColor = when {
        isChecked -> MaterialTheme.colorScheme.primary
        isToday -> MaterialTheme.colorScheme.primaryContainer
        else -> Color.Transparent
    }
    val contentColor = when {
        isChecked -> MaterialTheme.colorScheme.onPrimary
        isFuture -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.onSurface
    }
    val todayBorder = if (isToday) {
        BorderStroke(
            width = 2.dp,
            color = if (isChecked) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.primary
            },
        )
    } else {
        null
    }

    Surface(
        modifier = Modifier.size(38.dp),
        shape = CircleShape,
        color = backgroundColor,
        contentColor = contentColor,
        border = todayBorder,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isChecked || isToday) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}

internal fun calendarDates(month: YearMonth): List<LocalDate?> {
    val leadingEmptyDays = month.atDay(1).dayOfWeek.value - 1
    val dates = buildList<LocalDate?> {
        repeat(leadingEmptyDays) { add(null) }
        repeat(month.lengthOfMonth()) { dayIndex ->
            add(month.atDay(dayIndex + 1))
        }
    }
    val trailingEmptyDays = (7 - dates.size % 7) % 7
    return dates + List(trailingEmptyDays) { null }
}
