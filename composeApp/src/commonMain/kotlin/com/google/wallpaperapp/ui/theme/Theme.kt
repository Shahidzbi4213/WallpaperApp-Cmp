package com.google.wallpaperapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.wallpaperapp.core.platform.colorSchemeProvide

/* ============================================================
   SCREENY — dark-first sunset -> nebula theme.
   darkScheme carries the full re-skin; lightScheme is a clean
   brand-accented variant for the app's Light mode.
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

val lightScheme = lightColorScheme(
    primary = Ember,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0D2),
    onPrimaryContainer = Color(0xFF5A1B08),
    secondary = Violet,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE7DAFF),
    onSecondaryContainer = Color(0xFF2A0E52),
    tertiary = Magenta,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD9EC),
    onTertiaryContainer = Color(0xFF4E0A31),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Paper,
    onBackground = Ink900,
    surface = Color(0xFFFFFFFF),
    onSurface = Ink900,
    surfaceVariant = Color(0xFFE7E5F0),
    onSurfaceVariant = Color(0xFF48475A),
    outline = Color(0xFF79788A),
    outlineVariant = Color(0xFFCAC8D6),
    scrim = Color.Black,
    inverseSurface = Ink900,
    inverseOnSurface = Paper,
    inversePrimary = Color(0xFFFFB59B),
    surfaceDim = Color(0xFFDEDCEB),
    surfaceBright = Color(0xFFFFFFFF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF6F5FC),
    surfaceContainer = Color(0xFFF1F0F9),
    surfaceContainerHigh = Color(0xFFEBEAF4),
    surfaceContainerHighest = Color(0xFFE5E4EF),
)

@Composable
fun ScreenyTheme(
    dynamicColor: Boolean,
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {

    val colorScheme = colorSchemeProvide(
        isDynamicColor = dynamicColor,
        isDarkMode = darkTheme
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getAppTypography(),
        content = content
    )
}
