package com.google.wallpaperapp.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.components.Footer
import com.google.wallpaperapp.ui.components.LoadingPlaceHolder
import com.google.wallpaperapp.ui.components.WallpaperItem
import com.google.wallpaperapp.ui.composables.LazyPagingItems

@Composable
fun ShowWallpapers(
    wallpapers: LazyPagingItems<Wallpaper>,
    state: LazyGridState,
    onWallpaperClick: (wallpaper: Wallpaper, items: List<Wallpaper>) -> Unit
) {

    var showLoader by remember { mutableStateOf(false) }


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
        modifier = Modifier.fillMaxSize(),
    ) {

        if (showLoader) {
            items(20) { LoadingPlaceHolder() }
        }else{


            items(wallpapers.itemCount, key = { index ->
                wallpapers[index]?.id ?: "fallback_$index"
            }) { index ->
                val wallpaper = wallpapers[index]
                if (wallpaper != null) {
                    WallpaperItem(
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