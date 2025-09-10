package com.google.wallpaperapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.toFontFamily
import org.jetbrains.compose.resources.Font
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.poppins_regular

@Composable
fun getAppTypography(): Typography {
    val baseline = Typography()

    val screenyFontFamily = getScreenyFontFamily()

    val appTypography = Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = screenyFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = screenyFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = screenyFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = screenyFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = screenyFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = screenyFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = screenyFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = screenyFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = screenyFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = screenyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = screenyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = screenyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = screenyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = screenyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = screenyFontFamily),
    )
    return appTypography

}

@Composable
fun getScreenyFontFamily(): FontFamily {
  return Font(
        Res.font.poppins_regular
    ).toFontFamily()
}