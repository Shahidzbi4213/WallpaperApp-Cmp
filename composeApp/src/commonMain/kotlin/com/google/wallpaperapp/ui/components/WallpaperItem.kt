package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.composables.shimmerBrush
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage

@Composable
fun WallpaperItem(
    modifier: Modifier = Modifier,
    wallpaper: String,
    isForApply: Boolean = false,
    getPainter: (Painter) -> Unit = {},
    onWallpaperClick: (String) -> Unit = {}
) {

    var showShimmer by remember { mutableStateOf(false) }

    CoilImage(
        imageModel = { wallpaper },
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop,
            contentDescription = null,
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(
                shimmerBrush(targetValue = 1300f, showShimmer = showShimmer),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .height(200.dp)
            .clickable { onWallpaperClick(wallpaper) },
        loading = { state ->
            showShimmer = true
        },
        success = { state, painter ->
            showShimmer = false

            if (isForApply) {
                getPainter(painter)
            }
        }

    )


}

