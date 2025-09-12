package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.composables.carouselTransition

@Composable
fun SinglePageContent(
    wallpaperUrl: String,
    pagerState: PagerState, page: Int,
) {
    Box(
        modifier = Modifier
            .carouselTransition(page, pagerState)
            .padding(10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {


        WallpaperItem(
            wallpaper = wallpaperUrl,
            isForApply = true,
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth()
        )
    }
}


