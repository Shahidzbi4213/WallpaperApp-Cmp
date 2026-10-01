package com.google.wallpaperapp.ui.desktop.theme

import androidx.compose.ui.unit.dp

/**
 * Desktop layout constants. On desktop 1dp is roughly 1px, so phone-tuned sizes read small and
 * cramped -- these are chosen for a pointer and a monitor, not a thumb and a handset.
 */
object DesktopDimens {
    val SidebarWidth = 232.dp
    val SidebarCollapsedWidth = 76.dp

    /** Below this window width the sidebar drops to icons only. */
    val SidebarCollapseThreshold = 1100.dp

    /** Below this the detail view takes over instead of splitting the content area. */
    val SplitViewThreshold = 1400.dp

    val ToolbarHeight = 60.dp

    /** Grid cards target this width; the grid reflows from 3 columns at 1280 to 7+ at 2560. */
    val CardMinWidth = 300.dp
    val CardAspectRatio = 16f / 9f
    val CardCorner = 10.dp

    val Gutter = 24.dp
    val GridSpacing = 16.dp

    /** Reading-width cap so settings and forms don't stretch across an ultrawide display. */
    val FormMaxWidth = 720.dp

    val DetailPaneMinWidth = 380.dp
    val DetailPaneDefaultWidth = 520.dp

    /** Comfortable pointer target; the phone UI uses 42-44.dp which is tight with a mouse. */
    val IconButtonSize = 36.dp
    val RowHeight = 44.dp
}
