package com.example.dailycheckin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DailyCheckInColors = lightColorScheme(
    primary = Color(0xFF176B4D),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6F5E5),
    onPrimaryContainer = Color(0xFF082E21),
    background = Color(0xFFF8FAF8),
    surface = Color.White,
)

@Composable
fun DailyCheckInTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DailyCheckInColors,
        content = content,
    )
}

