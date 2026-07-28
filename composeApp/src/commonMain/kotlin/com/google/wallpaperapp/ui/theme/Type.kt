package com.google.wallpaperapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.sora_medium
import wallpaperapp.composeapp.generated.resources.sora_regular
import wallpaperapp.composeapp.generated.resources.sora_semibold
import wallpaperapp.composeapp.generated.resources.space_grotesk_bold
import wallpaperapp.composeapp.generated.resources.space_grotesk_medium
import wallpaperapp.composeapp.generated.resources.space_grotesk_regular
import wallpaperapp.composeapp.generated.resources.space_grotesk_semibold
import wallpaperapp.composeapp.generated.resources.space_mono_bold
import wallpaperapp.composeapp.generated.resources.space_mono_regular

/* ============================================================
   SCREENY — TYPOGRAPHY
   Futuristic 2026 pairing (see design system readme):
     Display : Space Grotesk  (titles, headings)
     UI/Body : Sora           (body, labels)
     Mono    : Space Mono     (eyebrows, tags, numerals)
   ============================================================ */

@Composable
fun screenyDisplayFamily(): FontFamily = FontFamily(
    Font(Res.font.space_grotesk_regular, FontWeight.Normal),
    Font(Res.font.space_grotesk_medium, FontWeight.Medium),
    Font(Res.font.space_grotesk_semibold, FontWeight.SemiBold),
    Font(Res.font.space_grotesk_bold, FontWeight.Bold),
)

@Composable
fun screenyBodyFamily(): FontFamily = FontFamily(
    Font(Res.font.sora_regular, FontWeight.Normal),
    Font(Res.font.sora_medium, FontWeight.Medium),
    Font(Res.font.sora_semibold, FontWeight.SemiBold),
)

@Composable
fun screenyMonoFamily(): FontFamily = FontFamily(
    Font(Res.font.space_mono_regular, FontWeight.Normal),
    Font(Res.font.space_mono_bold, FontWeight.Bold),
)

/** Display (Space Grotesk) family — kept for callers that reference it by name. */
@Composable
fun getScreenyFontFamily(): FontFamily = screenyDisplayFamily()

/**
 * Small mono uppercase eyebrow accent ("FEATURED COLLECTION · 2026"). Apply
 * `.uppercase()` to the text at the call site.
 */
@Composable
fun eyebrowStyle(): TextStyle = TextStyle(
    fontFamily = screenyMonoFamily(),
    fontWeight = FontWeight.Normal,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 2.4.sp,
)

@Composable
fun getAppTypography(): Typography {
    val display = screenyDisplayFamily()
    val body = screenyBodyFamily()

    return Typography(
        displayLarge = TextStyle(fontFamily = display, fontWeight = FontWeight.Bold, fontSize = 44.sp, lineHeight = 48.sp, letterSpacing = (-0.5).sp),
        displayMedium = TextStyle(fontFamily = display, fontWeight = FontWeight.Bold, fontSize = 36.sp, lineHeight = 40.sp, letterSpacing = (-0.5).sp),
        displaySmall = TextStyle(fontFamily = display, fontWeight = FontWeight.Bold, fontSize = 30.sp, lineHeight = 36.sp, letterSpacing = (-0.25).sp),
        headlineLarge = TextStyle(fontFamily = display, fontWeight = FontWeight.SemiBold, fontSize = 30.sp, lineHeight = 36.sp, letterSpacing = (-0.25).sp),
        headlineMedium = TextStyle(fontFamily = display, fontWeight = FontWeight.SemiBold, fontSize = 26.sp, lineHeight = 32.sp, letterSpacing = (-0.25).sp),
        headlineSmall = TextStyle(fontFamily = display, fontWeight = FontWeight.SemiBold, fontSize = 21.sp, lineHeight = 28.sp),
        titleLarge = TextStyle(fontFamily = display, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp, letterSpacing = (-0.2).sp),
        titleMedium = TextStyle(fontFamily = display, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp),
        titleSmall = TextStyle(fontFamily = display, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
        bodyLarge = TextStyle(fontFamily = body, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
        bodyMedium = TextStyle(fontFamily = body, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
        bodySmall = TextStyle(fontFamily = body, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp),
        labelLarge = TextStyle(fontFamily = body, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
        labelMedium = TextStyle(fontFamily = body, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
        labelSmall = TextStyle(fontFamily = body, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
    )
}
