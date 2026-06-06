package com.example.dailycheckin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailycheckin.ui.theme.BrandGreen
import com.example.dailycheckin.ui.theme.CheckedGreen
import com.example.dailycheckin.ui.theme.PrimaryText
import com.example.dailycheckin.ui.theme.SecondaryText
import com.example.dailycheckin.ui.theme.WeekendText
import java.time.LocalDate
import java.time.YearMonth

private val weekLabels = listOf("一", "二", "三", "四", "五", "六", "日")

@Composable
fun MonthCalendar(
    month: YearMonth,
    checkedDates: Set<LocalDate>,
    today: LocalDate,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            weekLabels.forEachIndexed { index, label ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        color = if (index >= 5) WeekendText else SecondaryText,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                    )
                }
            }
        }

        calendarDates(month).chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                week.forEach { date ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
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
    val background = when {
        isToday -> BrandGreen
        isChecked -> CheckedGreen
        else -> Color.Transparent
    }
    val foreground = when {
        isToday -> Color.White
        isChecked -> BrandGreen
        isFuture -> SecondaryText.copy(alpha = 0.55f)
        else -> PrimaryText
    }

    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(background),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = foreground,
            fontWeight = if (isToday || isChecked) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
            fontSize = 13.sp,
        )
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
