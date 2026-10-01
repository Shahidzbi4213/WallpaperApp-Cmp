package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect

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
    val canScroll by remember { derivedStateOf { state.canScrollBackward } }
    val topBlurRadius by animateDpAsState(
        targetValue = if (canScroll) 30.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "desktopGridTopBlur"
    )
    val density = LocalDensity.current
    val blurHeightPx = with(density) { 120.dp.toPx() }

    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minCellWidth),
            state = state,
            contentPadding = contentPadding,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(DesktopDimens.GridSpacing),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(DesktopDimens.GridSpacing),
            modifier = Modifier
                .fillMaxSize()
                .hazeEffect {
                    blurRadius = topBlurRadius
                    progressive = HazeProgressive.verticalGradient(
                        startY = 0f,
                        endY = blurHeightPx,
                        startIntensity = 1f,
                        endIntensity = 0f
                    )
                },
            content = content
        )

        DesktopGridScrollbar(
            state = state,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(vertical = 4.dp, horizontal = 2.dp)
        )
    }
}
