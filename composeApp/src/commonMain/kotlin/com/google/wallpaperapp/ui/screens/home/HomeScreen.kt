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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import com.google.wallpaperapp.core.platform.BackHandler
import com.google.wallpaperapp.data.remote.models.WallpaperResponse
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.components.Footer
import com.google.wallpaperapp.ui.components.LoadingPlaceHolder
import com.google.wallpaperapp.ui.components.WallpaperItem


@Composable
fun HomeScreen(
    wallpapers: LazyPagingItems<Wallpaper>,
    modifier: Modifier = Modifier,
    onWallpaperClick: (Wallpaper) -> Unit,
    onBack: () -> Unit
) {

    BackHandler(true, onBack = onBack)

    val state = rememberLazyGridState()

    LazyVerticalGrid(
        state = state,
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize(),

        ) {

        if (wallpapers.loadState.refresh == LoadState.Loading) {
            items(20) {
                LoadingPlaceHolder(modifier = Modifier.height(200.dp))
            }
        }


        items(wallpapers.itemCount, key = { "${wallpapers[it]?.id}.${wallpapers[it]?.id}" }) { index ->

            val wallpaper = wallpapers[index]
            if (wallpaper != null) {
                WallpaperItem(
                    wallpaper = wallpaper.portrait,
                ) {
                    onWallpaperClick(wallpaper)
                }
            }
        }

        if (wallpapers.loadState.append == LoadState.Loading)
            item(span = { GridItemSpan(this.maxLineSpan) }) {
                Footer()
            }


    }


}


