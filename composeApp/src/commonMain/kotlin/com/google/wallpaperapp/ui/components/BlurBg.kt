package com.google.wallpaperapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage

@Composable
fun BlurBg(wallpaperUrl: String,currentlyLoaded:(ImageBitmap?)-> Unit) {


        CoilImage(
            imageModel = { wallpaperUrl },
            imageOptions = ImageOptions(
                contentDescription = null,
                contentScale = ContentScale.Crop,
            ),
            success = { state, painter ->

                Crossfade(targetState = state) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().graphicsLayer {
                            renderEffect = BlurEffect(35f, 35f)
                        }
                    )
                }

                currentlyLoaded(state.imageBitmap)
            },
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f), Color.Transparent
                        )
                    )
                )
        )

}