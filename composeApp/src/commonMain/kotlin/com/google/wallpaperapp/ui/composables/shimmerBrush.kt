package com.google.wallpaperapp.ui.composables

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Premium dark glass shimmer sweep matching Screeny's design system.
 * Uses subtle, monochromatic neutral tones (#131620 -> #222737 -> #131620) with a clean
 * horizontal light sweep across cards, replacing harsh saturated color flashes.
 */
@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1200f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color(0xFF131620),
            Color(0xFF222737),
            Color(0xFF131620)
        )

        val transition = rememberInfiniteTransition(label = "Shimmer")
        val translateAnimation = transition.animateFloat(
            initialValue = -targetValue,
            targetValue = targetValue * 1.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(1600),
                repeatMode = RepeatMode.Restart
            ),
            label = "Shimmer"
        )

        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = translateAnimation.value, y = 0f),
            end = Offset(x = translateAnimation.value + targetValue * 0.7f, y = 140f)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}