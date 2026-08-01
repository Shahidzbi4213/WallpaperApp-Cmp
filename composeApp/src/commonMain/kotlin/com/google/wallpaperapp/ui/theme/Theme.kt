package com.google.wallpaperapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/* ============================================================
   SCREENY — dark-first sunset -> nebula theme.
   The app is dark-only; darkScheme is the single source of truth.
   ============================================================ */

val darkScheme = darkColorScheme(
    primary = Ember,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3A1B10),
    onPrimaryContainer = Color(0xFFFFD2BE),
    secondary = Violet,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF241043),
    onSecondaryContainer = Color(0xFFD8C4FF),
    tertiary = Magenta,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF3A0E28),
    onTertiaryContainer = Color(0xFFF9B8DC),
    error = Danger,
    onError = Color.White,
    errorContainer = Color(0xFF3A0E14),
    onErrorContainer = Color(0xFFFFB8BE),
    background = Void,
    onBackground = TextHi,
    surface = Ink900,
    onSurface = TextHi,
    surfaceVariant = Ink800,
    onSurfaceVariant = TextMid,
    outline = Ink600,
    outlineVariant = Ink800,
    scrim = Color.Black,
    inverseSurface = Paper,
    inverseOnSurface = Ink900,
    inversePrimary = Ember,
    surfaceDim = Void,
    surfaceBright = Ink750,
    surfaceContainerLowest = Void,
    surfaceContainerLow = Ink950,
    surfaceContainer = Ink900,
    surfaceContainerHigh = Ink850,
    surfaceContainerHighest = Ink800,
)

@Composable
fun ScreenyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = darkScheme,
        typography = getAppTypography(),
        content = content
    )
}
