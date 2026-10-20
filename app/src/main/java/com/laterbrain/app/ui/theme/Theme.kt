package com.laterbrain.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Calm palette: warm paper, deep sage, a touch of gold for "the moment".
private val Sage = Color(0xFF3F6B5C)
private val SageLight = Color(0xFFA6D0BF)
private val Gold = Color(0xFFC9972F)
private val GoldLight = Color(0xFFF2C46D)

private val LightColors = lightColorScheme(
    primary = Sage,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD5E8DF),
    onPrimaryContainer = Color(0xFF0F2A21),
    secondary = Color(0xFF5B6560),
    secondaryContainer = Color(0xFFE6EBE8),
    onSecondaryContainer = Color(0xFF1B211E),
    tertiary = Gold,
    tertiaryContainer = Color(0xFFFBEBC8),
    onTertiaryContainer = Color(0xFF3A2A05),
    background = Color(0xFFFAF8F4),
    onBackground = Color(0xFF1B1C1A),
    surface = Color(0xFFFAF8F4),
    onSurface = Color(0xFF1B1C1A),
    surfaceVariant = Color(0xFFEDEAE3),
    onSurfaceVariant = Color(0xFF4A4C48),
    surfaceContainerLow = Color(0xFFF4F1EB),
    surfaceContainer = Color(0xFFEFECE5),
    outline = Color(0xFF7A7C77),
    outlineVariant = Color(0xFFD3D0C8),
)

private val DarkColors = darkColorScheme(
    primary = SageLight,
    onPrimary = Color(0xFF0F2A21),
    primaryContainer = Color(0xFF274E40),
    onPrimaryContainer = Color(0xFFD5E8DF),
    secondary = Color(0xFFBFC9C3),
    secondaryContainer = Color(0xFF2D3431),
    onSecondaryContainer = Color(0xFFDDE5E0),
    tertiary = GoldLight,
    tertiaryContainer = Color(0xFF4F3B0C),
    onTertiaryContainer = Color(0xFFFBEBC8),
    background = Color(0xFF121413),
    onBackground = Color(0xFFE3E3DF),
    surface = Color(0xFF121413),
    onSurface = Color(0xFFE3E3DF),
    surfaceVariant = Color(0xFF2A2D2B),
    onSurfaceVariant = Color(0xFFC3C6C1),
    surfaceContainerLow = Color(0xFF1A1C1B),
    surfaceContainer = Color(0xFF1F2120),
    outline = Color(0xFF8D918C),
    outlineVariant = Color(0xFF3F4340),
)

private val AppTypography = Typography().let { base ->
    base.copy(
        headlineSmall = base.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
        titleLarge = base.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        titleMedium = base.titleMedium.copy(fontWeight = FontWeight.Medium),
        labelLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.1.sp),
    )
}

@Composable
fun LaterBrainTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content,
    )
}
