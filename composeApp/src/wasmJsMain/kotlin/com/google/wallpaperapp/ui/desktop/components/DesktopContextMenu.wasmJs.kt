package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.runtime.Composable

@Composable
actual fun DesktopContextMenuArea(
    items: () -> List<DesktopContextMenuItem>,
    content: @Composable () -> Unit
) {
    content()
}
