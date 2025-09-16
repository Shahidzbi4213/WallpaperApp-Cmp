package com.google.wallpaperapp.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
fun isDarkMode(appMode: AppMode): Boolean = when (appMode) {
    AppMode.DEFAULT -> isSystemInDarkTheme()
    AppMode.LIGHT -> false
    AppMode.DARK -> true
}

