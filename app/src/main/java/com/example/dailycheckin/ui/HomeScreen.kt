package com.example.dailycheckin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailycheckin.viewmodel.CheckInUiState
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    state: CheckInUiState,
    onOpenHistory: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "每日自动打卡",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = state.today.format(DATE_FORMATTER),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
        Spacer(modifier = Modifier.height(28.dp))

        when {
            state.isLoading -> CircularProgressIndicator()
            state.errorMessage != null -> Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
            )
            else -> CheckInContent(state, onOpenHistory)
        }
    }
}

@Composable
private fun CheckInContent(
    state: CheckInUiState,
    onOpenHistory: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = if (state.didCreateToday) {
                    "✓ 今天打卡成功"
                } else {
                    "✓ 今天已经打过卡"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = "打卡时间：${state.todayRecord?.checkInTime.orEmpty()}",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(
            label = "当前连续",
            value = "${state.currentStreak} 天",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = "历史最长",
            value = "${state.longestStreak} 天",
            modifier = Modifier.weight(1f),
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(
            label = "本月打卡",
            value = "${state.currentMonthDays} 天",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = "累计打卡",
            value = "${state.totalDays} 天",
            modifier = Modifier.weight(1f),
        )
    }

    Spacer(modifier = Modifier.height(20.dp))
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("本月打卡率")
                Text(
                    text = "${state.currentMonthRate}%",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            LinearProgressIndicator(
                progress = { state.currentMonthRate.coerceIn(0, 100) / 100f },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "按本月截至今天的天数计算",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
    Button(
        onClick = onOpenHistory,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("查看打卡月历与记录")
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

private val DATE_FORMATTER = DateTimeFormatter.ofPattern(
    "M月d日 EEEE",
    Locale.CHINA,
)
