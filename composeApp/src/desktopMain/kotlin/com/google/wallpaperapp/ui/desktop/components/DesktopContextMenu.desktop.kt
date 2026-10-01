package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.runtime.Composable

@Composable
actual fun DesktopContextMenuArea(
    items: () -> List<DesktopContextMenuItem>,
    content: @Composable () -> Unit
) {
    ContextMenuArea(
        items = { items().map { ContextMenuItem(it.label, it.onClick) } },
        content = content
    )
}
