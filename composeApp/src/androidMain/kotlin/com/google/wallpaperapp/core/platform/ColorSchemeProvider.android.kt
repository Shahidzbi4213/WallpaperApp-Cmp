package com.google.wallpaperapp.core.platform


import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.wallpaperapp.ui.theme.darkScheme
import com.google.wallpaperapp.ui.theme.lightScheme

@Composable
actual fun colorSchemeProvide(isDarkMode: Boolean,isDynamicColor: Boolean): ColorScheme {
    return when {
        isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        isDynamicColor -> darkScheme
        else -> lightScheme
    }
}