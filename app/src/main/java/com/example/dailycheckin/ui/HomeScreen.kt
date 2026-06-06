package com.example.dailycheckin.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailycheckin.ui.theme.AppBackground
import com.example.dailycheckin.ui.theme.BrandGreen
import com.example.dailycheckin.ui.theme.BrandGreenLight
import com.example.dailycheckin.ui.theme.CardBorder
import com.example.dailycheckin.ui.theme.MutedBackground
import com.example.dailycheckin.ui.theme.PrimaryText
import com.example.dailycheckin.ui.theme.SecondaryText
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
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = state.today.format(DATE_FORMATTER),
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = SecondaryText,
        )
        Text(
            text = "每日打卡",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryText,
            modifier = Modifier.padding(top = 3.dp),
        )
        Spacer(modifier = Modifier.height(22.dp))

        when {
            state.isLoading -> CircularProgressIndicator(color = BrandGreen)
            state.errorMessage != null -> Text(
                text = state.errorMessage,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.error,
            )
            else -> HomeContent(state, onOpenHistory)
        }
    }
}

@Composable
private fun HomeContent(
    state: CheckInUiState,
    onOpenHistory: () -> Unit,
) {
    CheckInBanner(state)
    Spacer(modifier = Modifier.height(14.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SmallStatCard(
            icon = "📅",
            value = state.currentMonthDays.toString(),
            label = "本月打卡天数",
            modifier = Modifier.weight(1f),
        )
        SmallStatCard(
            icon = "🗓",
            value = (state.today.lengthOfMonth() - state.today.dayOfMonth).toString(),
            label = "距月底还有",
            modifier = Modifier.weight(1f),
        )
    }
    Spacer(modifier = Modifier.height(14.dp))

    MonthProgressCard(state)
    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onOpenHistory,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandGreen),
    ) {
        Text(
            text = "查看打卡月历与记录  →",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun CheckInBanner(state: CheckInUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(BrandGreen, BrandGreenLight),
                ),
            )
            .padding(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(Color.White.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "✓",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                )
            }
            Column {
                Text(
                    text = "今天已打卡 🎉",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                )
                Text(
                    text = "${state.todayRecord?.checkInTime.orEmpty()} 完成",
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BannerStat(
                value = state.currentStreak.toString(),
                label = "当前连续",
                modifier = Modifier.weight(1f),
            )
            BannerDivider()
            BannerStat(
                value = state.longestStreak.toString(),
                label = "历史最长",
                modifier = Modifier.weight(1f),
            )
            BannerDivider()
            BannerStat(
                value = "${state.currentMonthRate}%",
                label = "本月完成率",
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun BannerStat(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.75f),
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
        )
    }
}

@Composable
private fun BannerDivider() {
    Box(
        modifier = Modifier
            .size(width = 1.dp, height = 34.dp)
            .background(Color.White.copy(alpha = 0.25f)),
    )
}

@Composable
private fun SmallStatCard(
    icon: String,
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
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = icon,
                color = BrandGreen,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
            )
            Text(
                text = value,
                color = PrimaryText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
            Text(
                text = label,
                color = SecondaryText,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
            )
        }
    }
}

@Composable
private fun MonthProgressCard(state: CheckInUiState) {
    val checkedDays = state.records.mapNotNull { record ->
        runCatching { java.time.LocalDate.parse(record.date) }.getOrNull()
    }.filter {
        it.year == state.today.year && it.month == state.today.month
    }.map { it.dayOfMonth }.toSet()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "本月打卡进度",
                    color = PrimaryText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
                Text(
                    text = "${state.currentMonthDays} / ${state.today.dayOfMonth}天",
                    color = BrandGreen,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
            }
            LinearProgressIndicator(
                progress = { state.currentMonthRate.coerceIn(0, 100) / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(12.dp)),
                color = BrandGreen,
                trackColor = MutedBackground,
            )
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                (1..state.today.dayOfMonth).forEach { day ->
                    val done = day in checkedDays
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (done) BrandGreen else MutedBackground)
                            .then(
                                if (day == state.today.dayOfMonth) {
                                    Modifier.border(
                                        1.dp,
                                        BrandGreen,
                                        RoundedCornerShape(12.dp),
                                    )
                                } else {
                                    Modifier
                                },
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = if (done) "✓" else day.toString(),
                            color = if (done) Color.White else SecondaryText,
                            fontWeight = if (done) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Normal
                            },
                            fontSize = 9.sp,
                        )
                    }
                }
            }
        }
    }
}

private val DATE_FORMATTER = DateTimeFormatter.ofPattern(
    "M月d日 EEEE",
    Locale.CHINA,
)
