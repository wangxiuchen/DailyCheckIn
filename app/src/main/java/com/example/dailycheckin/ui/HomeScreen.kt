package com.example.dailycheckin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailycheckin.viewmodel.CheckInUiState

@Composable
fun HomeScreen(
    state: CheckInUiState,
    onOpenHistory: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "每日自动打卡",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(40.dp))

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
                text = "✓ 今日已打卡",
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

    Spacer(modifier = Modifier.height(28.dp))
    Text(
        text = "已连续打卡 ${state.currentStreak} 天",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = "累计打卡 ${state.totalDays} 天",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(36.dp))
    Button(
        onClick = onOpenHistory,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("查看历史记录")
    }
}

