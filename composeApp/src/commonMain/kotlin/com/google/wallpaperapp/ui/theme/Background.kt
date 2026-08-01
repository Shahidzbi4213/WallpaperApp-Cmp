package com.google.wallpaperapp.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * The Screeny app backdrop: the near-black "void" dusted with two faint radial
 * aurora glows — ember top-left, violet bottom-right. No patterns, no noise;
 * full-bleed imagery and neon accents pop against it.
 */
fun Modifier.auroraBackground(): Modifier = drawBehind {
    drawRect(Void)
    drawRect(
        Brush.radialGradient(
            colors = listOf(Ember.copy(alpha = 0.16f), Color.Transparent),
            center = Offset(size.width * 0.08f, size.height * 0.02f),
            radius = size.maxDimension * 0.5f
        )
    )
    drawRect(
        Brush.radialGradient(
            colors = listOf(Violet.copy(alpha = 0.18f), Color.Transparent),
            center = Offset(size.width * 0.94f, size.height * 0.98f),
            radius = size.maxDimension * 0.55f
        )
    )
}
