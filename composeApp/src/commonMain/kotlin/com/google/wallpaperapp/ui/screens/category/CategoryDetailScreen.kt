package com.google.wallpaperapp.ui.screens.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import com.google.wallpaperapp.core.platform.BackHandler
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.components.Footer
import com.google.wallpaperapp.ui.components.LoadingPlaceHolder
import com.google.wallpaperapp.ui.components.WallpaperItem
import com.google.wallpaperapp.ui.composables.LazyPagingItems
import com.google.wallpaperapp.ui.theme.getScreenyFontFamily

@Composable
fun CategoryDetailScreen(
    title: String,
    wallpapers: LazyPagingItems<Wallpaper>,
    onWallpaperClick: (Wallpaper) -> Unit,
    onBackClick: () -> Unit
) {


    BackHandler(true,onBackClick)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ToolBar(title = title, onBackClick = onBackClick)


        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) {

            if (wallpapers.loadState.refresh == LoadState.Loading) {
                items(20) {
                    LoadingPlaceHolder(modifier = Modifier.height(200.dp))
                }
            }

            items(
                count = wallpapers.itemCount,
                key = { "${wallpapers[it]?.id}_$it" }
            ) { index ->
                wallpapers[index]?.let { wallpaper ->
                    WallpaperItem(
                        wallpaper = wallpaper.portrait,) {
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
}

@Composable
fun ToolBar(title: String, modifier: Modifier = Modifier, onBackClick: () -> Unit) {

    Row(
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth()
            .padding(start = 10.dp),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier
                .wrapContentSize()
                .clickable { onBackClick() }


        )

        Text(
            text = title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            fontFamily = getScreenyFontFamily(), modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(end = 30.dp),
            textAlign = TextAlign.Center

        )
    }

}