package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun SplashProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    shape: Shape = RoundedCornerShape(16.dp)
) {


    Box(
        modifier = modifier
            .clip(shape)
            .background(trackColor)
            .fillMaxWidth(0.4f)
            .height(6.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(progressColor)
                .fillMaxHeight()
                .fillMaxWidth(progress)
        ) {

        }
    }
}