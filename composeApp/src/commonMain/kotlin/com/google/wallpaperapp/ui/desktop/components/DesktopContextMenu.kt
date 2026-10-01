package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.runtime.Composable

data class DesktopContextMenuItem(
    val label: String,
    val onClick: () -> Unit
)

@Composable
expect fun DesktopContextMenuArea(
    items: () -> List<DesktopContextMenuItem>,
    content: @Composable () -> Unit
)
