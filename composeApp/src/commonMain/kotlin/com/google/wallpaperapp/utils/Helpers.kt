package com.google.wallpaperapp.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@ReadOnlyComposable
@Composable
fun currentAppMode(appMode: AppMode): Boolean = when (appMode) {
    AppMode.DEFAULT -> isSystemInDarkTheme()
    AppMode.LIGHT -> false
    AppMode.DARK -> true
}

