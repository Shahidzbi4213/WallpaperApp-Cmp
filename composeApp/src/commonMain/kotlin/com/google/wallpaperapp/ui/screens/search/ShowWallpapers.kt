package com.google.wallpaperapp.ui.screens.search

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.components.Footer
import com.google.wallpaperapp.ui.components.LoadingPlaceHolder
import com.google.wallpaperapp.ui.components.WallpaperItem
import com.google.wallpaperapp.ui.composables.LazyPagingItems
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect

@Composable
fun ShowWallpapers(
    wallpapers: LazyPagingItems<Wallpaper>,
    state: LazyGridState,
    onWallpaperClick: (wallpaper: Wallpaper, items: List<Wallpaper>) -> Unit
) {

    var showLoader by remember { mutableStateOf(false) }

    val canScroll by remember { derivedStateOf { state.canScrollBackward } }
    val topBlurRadius by animateDpAsState(
        targetValue = if (canScroll) 30.dp else 0.dp,
        animationSpec = tween(durationMillis = 250),
        label = "searchGridTopBlur"
    )
    val density = LocalDensity.current
    val blurHeightPx = with(density) { 140.dp.toPx() }

    LaunchedEffect(wallpapers.loadState.refresh){
        when(wallpapers.loadState.refresh){
            is LoadState.Error -> {
                showLoader = true
            }
            LoadState.Loading -> {
                showLoader = true
            }
            is LoadState.NotLoading ->{
                showLoader = false
                state.scrollToItem(0)
            }
        }
    }



    LazyVerticalGrid(
        state = state,
        overscrollEffect = null,
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
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
    ) {

        if (showLoader) {
            items(20) { LoadingPlaceHolder(modifier = Modifier.height(200.dp)) }
        }else{


            items(wallpapers.itemCount, key = { index ->
                wallpapers.peek(index)?.id ?: "fallback_$index"
            }) { index ->
                val wallpaper = wallpapers[index]
                if (wallpaper != null) {
                    WallpaperItem(
                        modifier = Modifier.height(200.dp),
                        wallpaper = wallpaper.portrait,
                        onWallpaperClick = { onWallpaperClick(wallpaper, wallpapers.itemSnapshotList.items) }
                    )
                }
            }

            if (wallpapers.loadState.append == LoadState.Loading)
                item(span = { GridItemSpan(this.maxLineSpan) }) {
                Footer()
            }
        }





    }
}