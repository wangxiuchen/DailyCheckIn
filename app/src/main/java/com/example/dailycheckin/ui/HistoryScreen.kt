package com.example.dailycheckin.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailycheckin.data.CheckInRecord
import com.example.dailycheckin.ui.theme.AppBackground
import com.example.dailycheckin.ui.theme.BrandGreen
import com.example.dailycheckin.ui.theme.CardBorder
import com.example.dailycheckin.ui.theme.MutedBackground
import com.example.dailycheckin.ui.theme.PrimaryText
import com.example.dailycheckin.ui.theme.SecondaryText
import com.example.dailycheckin.ui.theme.TertiaryText
import com.example.dailycheckin.viewmodel.CheckInUiState
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun HistoryScreen(
    state: CheckInUiState,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)
    var displayMode by remember { mutableStateOf(HistoryDisplayMode.CALENDAR) }
    var visibleMonth by remember { mutableStateOf(YearMonth.from(state.today)) }
    LaunchedEffect(state.today) {
        visibleMonth = YearMonth.from(state.today)
    }
    val checkedDates = remember(state.records) {
        state.records.mapNotNull { record ->
            runCatching { LocalDate.parse(record.date) }.getOrNull()
        }.toSet()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = onBack,
                modifier = Modifier.size(40.dp),
            ) {
                Text(
                    text = "‹",
                    color = PrimaryText,
                    fontWeight = FontWeight.Normal,
                    fontSize = 28.sp,
                )
            }
            Text(
                text = "打卡历史",
                color = PrimaryText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 4.dp),
            )
        }

        HistoryTabs(
            selected = displayMode,
            onSelected = { displayMode = it },
        )
        Spacer(modifier = Modifier.height(14.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (displayMode) {
                HistoryDisplayMode.CALENDAR -> CalendarContent(
                    state = state,
                    visibleMonth = visibleMonth,
                    checkedDates = checkedDates,
                    onPreviousMonth = { visibleMonth = visibleMonth.minusMonths(1) },
                    onNextMonth = { visibleMonth = visibleMonth.plusMonths(1) },
                )

                HistoryDisplayMode.LIST -> HistoryList(state)
            }
        }
    }
}

@Composable
private fun HistoryTabs(
    selected: HistoryDisplayMode,
    onSelected: (HistoryDisplayMode) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MutedBackground)
            .padding(3.dp),
    ) {
        HistoryDisplayMode.entries.forEach { mode ->
            val isSelected = mode == selected
            TextButton(
                onClick = { onSelected(mode) },
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) BrandGreen else Color.Transparent),
            ) {
                Text(
                    text = if (mode == HistoryDisplayMode.CALENDAR) "月历" else "列表",
                    color = if (isSelected) Color.White else TertiaryText,
                    fontWeight = if (isSelected) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    },
                    fontSize = 13.sp,
                )
            }
        }
    }
}

@Composable
private fun CalendarContent(
    state: CheckInUiState,
    visibleMonth: YearMonth,
    checkedDates: Set<LocalDate>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
) {
    val monthCheckedDays = checkedDates.count { YearMonth.from(it) == visibleMonth }
    val elapsedDays = when {
        visibleMonth < YearMonth.from(state.today) -> visibleMonth.lengthOfMonth()
        visibleMonth == YearMonth.from(state.today) -> state.today.dayOfMonth
        else -> 0
    }
    val completionRate = if (elapsedDays == 0) {
        0
    } else {
        (monthCheckedDays * 100.0 / elapsedDays).roundToInt()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CalendarStat(
                value = monthCheckedDays.toString(),
                label = "本月打卡",
                modifier = Modifier.weight(1f),
            )
            CalendarStat(
                value = "$completionRate%",
                label = "完成率",
                modifier = Modifier.weight(1f),
            )
            CalendarStat(
                value = state.currentStreak.toString(),
                label = "连续天数",
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MonthArrow(text = "‹", onClick = onPreviousMonth)
            Text(
                text = "${visibleMonth.year}年${visibleMonth.monthValue}月",
                color = PrimaryText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
            MonthArrow(text = "›", onClick = onNextMonth)
        }

        MonthCalendar(
            month = visibleMonth,
            checkedDates = checkedDates,
            today = state.today,
            modifier = Modifier.padding(top = 10.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        CalendarLegend()
    }
}

@Composable
private fun CalendarStat(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                color = BrandGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            )
            Text(
                text = label,
                color = SecondaryText,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
            )
        }
    }
}

@Composable
private fun MonthArrow(
    text: String,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.size(40.dp),
    ) {
        Text(
            text = text,
            color = SecondaryText,
            fontWeight = FontWeight.Normal,
            fontSize = 26.sp,
        )
    }
}

@Composable
private fun CalendarLegend() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LegendMark(color = BrandGreen)
        Text(
            text = "已打卡",
            color = SecondaryText,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
        )
        Spacer(modifier = Modifier.size(6.dp))
        LegendMark(color = MutedBackground)
        Text(
            text = "未打卡",
            color = SecondaryText,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
        )
    }
}

@Composable
private fun LegendMark(color: Color) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color),
    )
}

@Composable
private fun HistoryList(state: CheckInUiState) {
    if (state.records.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "暂无打卡记录",
                color = SecondaryText,
                fontWeight = FontWeight.Normal,
            )
        }
        return
    }

    val currentMonth = YearMonth.from(state.today)
    val groupedRecords = state.records.groupBy { record ->
        YearMonth.from(LocalDate.parse(record.date))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(key = "summary") {
            MonthSummaryCard(state)
        }

        groupedRecords.forEach { (month, monthRecords) ->
            item(key = "title-$month") {
                Text(
                    text = if (month == currentMonth) {
                        "${month.monthValue}月记录"
                    } else {
                        "${month.year}年${month.monthValue}月记录"
                    },
                    color = SecondaryText,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 2.dp),
                )
            }
            items(
                items = monthRecords,
                key = { it.id },
            ) { record ->
                HistoryRow(record)
            }
        }

        item(key = "end") {
            Text(
                text = "— 已显示全部记录 —",
                color = SecondaryText.copy(alpha = 0.65f),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

@Composable
private fun MonthSummaryCard(state: CheckInUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column {
                    Text(
                        text = "${state.today.year}年${state.today.monthValue}月",
                        color = PrimaryText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "已过${state.today.dayOfMonth}天，打卡${state.currentMonthDays}次",
                        color = SecondaryText,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
                Text(
                    text = "${state.currentMonthRate}%",
                    color = BrandGreen,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                )
            }
            Row(
                modifier = Modifier.padding(top = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                ProgressRing(
                    progress = state.currentMonthRate / 100f,
                    centerText = state.currentMonthDays.toString(),
                )
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    SummaryLine("本月打卡", "${state.currentMonthDays}天")
                    SummaryLine("当前连续", "${state.currentStreak}天")
                    SummaryLine("历史最长", "${state.longestStreak}天")
                }
            }
        }
    }
}

@Composable
private fun ProgressRing(
    progress: Float,
    centerText: String,
) {
    Box(
        modifier = Modifier.size(64.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
            val inset = stroke.width / 2
            val arcSize = Size(
                width = size.width - inset * 2,
                height = size.height - inset * 2,
            )
            drawArc(
                color = MutedBackground,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = stroke,
            )
            drawArc(
                color = BrandGreen,
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = stroke,
            )
        }
        Text(
            text = centerText,
            color = BrandGreen,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun SummaryLine(
    label: String,
    value: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(
            text = label,
            color = TertiaryText,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        )
        Text(
            text = value,
            color = PrimaryText,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun HistoryRow(record: CheckInRecord) {
    val date = LocalDate.parse(record.date)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .background(BrandGreen, CircleShape),
                )
                Column {
                    Text(
                        text = record.date,
                        color = PrimaryText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = date.format(WEEKDAY_FORMATTER),
                        color = SecondaryText,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
            }
            Text(
                text = "${record.checkInTime}  ✓",
                color = BrandGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
            )
        }
    }
}

private enum class HistoryDisplayMode {
    CALENDAR,
    LIST,
}

private val WEEKDAY_FORMATTER = DateTimeFormatter.ofPattern(
    "EEEE",
    Locale.CHINA,
)
