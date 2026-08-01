package com.google.wallpaperapp.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.components.Footer
import com.google.wallpaperapp.ui.components.LoadingPlaceHolder
import com.google.wallpaperapp.ui.components.WallpaperItem
import com.google.wallpaperapp.ui.composables.LazyPagingItems


@Composable
fun HomeScreen(
    wallpapers: LazyPagingItems<Wallpaper>,
    modifier: Modifier = Modifier,
    onWallpaperClick: (Wallpaper) -> Unit
) {

    val state = androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState()

    androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid(
        state = state,
        columns = androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Fixed(3),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
        overscrollEffect = null,
        modifier = modifier
            .fillMaxSize(),

        ) {

        if (wallpapers.loadState.refresh == LoadState.Loading) {
            items(20) {
                LoadingPlaceHolder(modifier = Modifier.height(200.dp))
            }
        }


        items(wallpapers.itemCount, key = { index ->
            val item = wallpapers.peek(index)
            item?.id ?: "fallback_$index"
        }) { index ->
            val wallpaper = remember { wallpapers[index] }

            if (wallpaper != null) {
                val height = remember(wallpaper.id) {
                    val hash = kotlin.math.abs(wallpaper.id.hashCode()) % 3
                    when (hash) {
                        0 -> 200.dp
                        1 -> 250.dp
                        else -> 300.dp
                    }
                }
                WallpaperItem(
                    modifier = Modifier.height(height),
                    wallpaper = wallpaper.portrait,
                ) {
                    onWallpaperClick(wallpaper)
                }
            }
        }

        if (wallpapers.loadState.append == LoadState.Loading)
            item(span = androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan.FullLine) {
                Footer()
            }


    }


}


