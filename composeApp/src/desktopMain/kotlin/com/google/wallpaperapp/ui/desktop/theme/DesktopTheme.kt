package com.google.wallpaperapp.ui.desktop.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.wallpaperapp.ui.theme.darkScheme
import com.google.wallpaperapp.ui.theme.screenyBodyFamily
import com.google.wallpaperapp.ui.theme.screenyDisplayFamily

/**
 * Same Screeny colours, same three fonts -- only the type scale changes.
 *
 * The shared scale is tuned for a phone held at arm's length. At desktop density (1dp ~ 1px)
 * on a monitor at 60cm, those sizes read noticeably small, so headings and titles step up while
 * body text stays close to the original.
 */
@Composable
fun DesktopTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkScheme,
        typography = desktopTypography(),
        content = content
    )
}

@Composable
private fun desktopTypography(): Typography {
    val display = screenyDisplayFamily()
    val body = screenyBodyFamily()

    return Typography(
        displayLarge = TextStyle(fontFamily = display, fontWeight = FontWeight.Bold, fontSize = 52.sp, lineHeight = 58.sp, letterSpacing = (-0.5).sp),
        displayMedium = TextStyle(fontFamily = display, fontWeight = FontWeight.Bold, fontSize = 42.sp, lineHeight = 48.sp, letterSpacing = (-0.5).sp),
        displaySmall = TextStyle(fontFamily = display, fontWeight = FontWeight.Bold, fontSize = 34.sp, lineHeight = 40.sp, letterSpacing = (-0.25).sp),
        headlineLarge = TextStyle(fontFamily = display, fontWeight = FontWeight.SemiBold, fontSize = 32.sp, lineHeight = 38.sp, letterSpacing = (-0.25).sp),
        headlineMedium = TextStyle(fontFamily = display, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 34.sp, letterSpacing = (-0.25).sp),
        headlineSmall = TextStyle(fontFamily = display, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 30.sp),
        titleLarge = TextStyle(fontFamily = display, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = (-0.2).sp),
        titleMedium = TextStyle(fontFamily = display, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 24.sp),
        titleSmall = TextStyle(fontFamily = display, fontWeight = FontWeight.Medium, fontSize = 15.sp, lineHeight = 22.sp),
        bodyLarge = TextStyle(fontFamily = body, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
        bodyMedium = TextStyle(fontFamily = body, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 21.sp),
        bodySmall = TextStyle(fontFamily = body, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp),
        labelLarge = TextStyle(fontFamily = body, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
        labelMedium = TextStyle(fontFamily = body, fontWeight = FontWeight.Medium, fontSize = 13.sp, lineHeight = 18.sp),
        labelSmall = TextStyle(fontFamily = body, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
    )
}
