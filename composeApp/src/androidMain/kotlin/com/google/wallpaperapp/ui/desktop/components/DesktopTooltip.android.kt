package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun DesktopTooltip(
    text: String,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    content()
}
