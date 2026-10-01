package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun DesktopTooltip(
    text: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
)
