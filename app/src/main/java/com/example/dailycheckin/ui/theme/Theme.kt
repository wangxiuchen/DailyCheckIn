package com.example.dailycheckin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

val BrandGreen = Color(0xFF2D6A4F)
val BrandGreenLight = Color(0xFF40916C)
val CheckedGreen = Color(0xFFD8EFDF)
val AppBackground = Color(0xFFFAF9F7)
val CardBorder = Color(0xFFEBE9E1)
val MutedBackground = Color(0xFFEDECEA)
val PrimaryText = Color(0xFF1A1A16)
val SecondaryText = Color(0xFF9B9B8E)
val TertiaryText = Color(0xFF7A7A72)
val WeekendText = Color(0xFFE8997A)

private val DailyCheckInColors = lightColorScheme(
    primary = BrandGreen,
    onPrimary = Color.White,
    primaryContainer = CheckedGreen,
    onPrimaryContainer = BrandGreen,
    background = AppBackground,
    onBackground = PrimaryText,
    surface = Color.White,
    onSurface = PrimaryText,
    surfaceVariant = MutedBackground,
    onSurfaceVariant = SecondaryText,
    outline = CardBorder,
)

private val DailyCheckInTypography = Typography().run {
    copy(
        displayLarge = displayLarge.regular(),
        displayMedium = displayMedium.regular(),
        displaySmall = displaySmall.regular(),
        headlineLarge = headlineLarge.semiBold(),
        headlineMedium = headlineMedium.semiBold(),
        headlineSmall = headlineSmall.semiBold(),
        titleLarge = titleLarge.semiBold(),
        titleMedium = titleMedium.semiBold(),
        titleSmall = titleSmall.semiBold(),
        bodyLarge = bodyLarge.regular(),
        bodyMedium = bodyMedium.regular(),
        bodySmall = bodySmall.regular(),
        labelLarge = labelLarge.semiBold(),
        labelMedium = labelMedium.regular(),
        labelSmall = labelSmall.regular(),
    )
}

private val DailyCheckInShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(14.dp),
    extraLarge = RoundedCornerShape(14.dp),
)

private fun TextStyle.regular() = copy(fontWeight = FontWeight.Normal)

private fun TextStyle.semiBold() = copy(fontWeight = FontWeight.SemiBold)

@Composable
fun DailyCheckInTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DailyCheckInColors,
        typography = DailyCheckInTypography,
        shapes = DailyCheckInShapes,
        content = content,
    )
}
