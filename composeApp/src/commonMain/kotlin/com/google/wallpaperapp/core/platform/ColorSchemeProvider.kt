package com.google.wallpaperapp.core.platform

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
expect fun colorSchemeProvide(isDarkMode: Boolean,isDynamicColor: Boolean): ColorScheme
