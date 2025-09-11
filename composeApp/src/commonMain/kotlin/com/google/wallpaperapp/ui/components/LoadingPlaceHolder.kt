package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.composables.shimmerBrush

@Composable
fun LoadingPlaceHolder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                shimmerBrush(targetValue = 1300f, showShimmer = true),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
    )
}
