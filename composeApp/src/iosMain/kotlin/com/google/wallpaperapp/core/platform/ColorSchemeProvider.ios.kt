package com.google.wallpaperapp.core.platform

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import com.google.wallpaperapp.ui.theme.darkScheme
import com.google.wallpaperapp.ui.theme.lightScheme

@Composable
actual fun colorSchemeProvide(isDarkMode: Boolean, isDynamicColor: Boolean): ColorScheme {
    return when {
        isDarkMode -> darkScheme
        else -> lightScheme
    }
}