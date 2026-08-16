package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens

/**
 * An adaptive wallpaper grid with the scrollbar a desktop user expects. [GridCells.Adaptive]
 * means the same screen goes from three columns on a 1280 window to seven or more on a 4K one,
 * with no breakpoints to maintain.
 */
@Composable
fun ScrollableGrid(
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    minCellWidth: androidx.compose.ui.unit.Dp = DesktopDimens.CardMinWidth,
    contentPadding: PaddingValues = PaddingValues(
        start = DesktopDimens.Gutter,
        end = DesktopDimens.Gutter,
        top = 4.dp,
        bottom = DesktopDimens.Gutter
    ),
    content: LazyGridScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minCellWidth),
            state = state,
            contentPadding = contentPadding,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(DesktopDimens.GridSpacing),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(DesktopDimens.GridSpacing),
            modifier = Modifier.fillMaxSize(),
            content = content
        )

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(state),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(vertical = 4.dp, horizontal = 2.dp)
        )
    }
}
