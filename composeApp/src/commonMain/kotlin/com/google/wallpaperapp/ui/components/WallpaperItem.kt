package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.composables.shimmerBrush
import com.google.wallpaperapp.ui.theme.GlassBorder
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.crossfade.CrossfadePlugin

@Composable
fun WallpaperItem(
    modifier: Modifier = Modifier,
    wallpaper: String,
    isForApply: Boolean = false,
    getPainter: (Painter) -> Unit = {},
    onWallpaperClick: (String) -> Unit = {}
) {

    var showShimmer by remember { mutableStateOf(true) }

    val shape = RoundedCornerShape(12.dp)

    CoilImage(
        imageModel = { wallpaper },
        imageOptions = ImageOptions(
            contentDescription = null,
            contentScale = ContentScale.Fit
        ),
        component = rememberImageComponent {
            CrossfadePlugin(250)
        },
        modifier = modifier
            .height(200.dp)
            .shadow(6.dp, shape)
            .clip(shape)
            .background(
                shimmerBrush(targetValue = 1300f, showShimmer = showShimmer),
                shape = shape
            )
            .border(1.dp, GlassBorder, shape)
            .clickable { onWallpaperClick(wallpaper) },
        success = { state, painter ->
            showShimmer = false

            if (isForApply) {
                getPainter(painter)
            }

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
            )
        },
    )
}
