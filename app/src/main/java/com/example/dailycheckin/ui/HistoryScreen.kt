package com.example.dailycheckin.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dailycheckin.data.CheckInRecord
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistoryScreen(
    records: List<CheckInRecord>,
    today: LocalDate,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)
    var displayMode by remember { mutableStateOf(HistoryDisplayMode.CALENDAR) }
    var visibleMonth by remember { mutableStateOf(YearMonth.from(today)) }
    LaunchedEffect(today) {
        visibleMonth = YearMonth.from(today)
    }
    val checkedDates = remember(records) {
        records.mapNotNull { record ->
            runCatching { LocalDate.parse(record.date) }.getOrNull()
        }.toSet()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onBack) {
                Text("返回")
            }
            Text(
                text = "打卡历史",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ModeButton(
                text = "月历",
                selected = displayMode == HistoryDisplayMode.CALENDAR,
                onClick = { displayMode = HistoryDisplayMode.CALENDAR },
                modifier = Modifier.weight(1f),
            )
            ModeButton(
                text = "列表",
                selected = displayMode == HistoryDisplayMode.LIST,
                onClick = { displayMode = HistoryDisplayMode.LIST },
                modifier = Modifier.weight(1f),
            )
        }

        when (displayMode) {
            HistoryDisplayMode.CALENDAR -> {
                MonthCalendar(
                    month = visibleMonth,
                    checkedDates = checkedDates,
                    today = today,
                    onPreviousMonth = { visibleMonth = visibleMonth.minusMonths(1) },
                    onNextMonth = { visibleMonth = visibleMonth.plusMonths(1) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                )
            }

            HistoryDisplayMode.LIST -> HistoryList(records)
        }
    }
}

@Composable
private fun ModeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (selected) {
        Button(onClick = onClick, modifier = modifier) {
            Text(text)
        }
    } else {
        OutlinedButton(onClick = onClick, modifier = modifier) {
            Text(text)
        }
    }
}

@Composable
private fun HistoryList(records: List<CheckInRecord>) {
    if (records.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("暂无打卡记录")
        }
        return
    }

    val groupedRecords = records.groupBy { record ->
        YearMonth.from(LocalDate.parse(record.date))
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        groupedRecords.forEach { (month, monthRecords) ->
            item(key = "month-$month") {
                Text(
                    text = "${month.year} 年 ${month.monthValue} 月",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 24.dp,
                        bottom = 8.dp,
                    ),
                )
            }
            items(
                items = monthRecords,
                key = { it.id },
            ) { record ->
                HistoryRow(record)
            }
        }
    }
}

@Composable
private fun HistoryRow(record: CheckInRecord) {
    val date = LocalDate.parse(record.date)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(record.date, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = date.format(WEEKDAY_FORMATTER),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            record.checkInTime,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
        )
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
}

private enum class HistoryDisplayMode {
    CALENDAR,
    LIST,
}

private val WEEKDAY_FORMATTER = DateTimeFormatter.ofPattern(
    "EEEE",
    Locale.CHINA,
)
