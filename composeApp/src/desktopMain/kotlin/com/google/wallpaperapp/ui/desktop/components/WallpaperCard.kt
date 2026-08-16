package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.composables.shimmerBrush
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import com.google.wallpaperapp.ui.theme.GlassBorder
import com.google.wallpaperapp.ui.theme.GlassBorderHi
import com.google.wallpaperapp.ui.theme.ScrimGradient
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage

/**
 * The desktop grid tile: a uniform 16:9 frame rather than the phone's hash-derived fake masonry.
 * Uniform cards are the native photo-browser idiom, they make landscape crops legible, and they
 * let the grid reflow cleanly at any column count.
 */
@Composable
fun WallpaperCard(
    imageUrl: String,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    overlay: @Composable BoxScope.(hovered: Boolean) -> Unit = {}
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    val scale by animateFloatAsState(
        targetValue = if (hovered) 1.02f else 1f,
        animationSpec = tween(140),
        label = "cardScale"
    )

    val shape = RoundedCornerShape(DesktopDimens.CardCorner)
    val borderColor = when {
        selected -> Color.White.copy(alpha = 0.75f)
        hovered -> GlassBorderHi
        else -> GlassBorder
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(DesktopDimens.CardAspectRatio)
            .scale(scale)
            .clip(shape)
            .border(if (selected) 2.dp else 1.dp, borderColor, shape)
            .desktopClickable(interaction = interaction, onClick = onClick)
    ) {
        CoilImage(
            imageModel = { imageUrl },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                contentDescription = contentDescription
            ),
            loading = {
                Box(Modifier.fillMaxSize().background(shimmerBrush()))
            },
            modifier = Modifier.fillMaxSize()
        )

        // Scrim only while hovered, so the grid stays clean at rest.
        AnimatedVisibility(
            visible = hovered,
            enter = fadeIn(tween(140)),
            exit = fadeOut(tween(140)),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(Modifier.fillMaxSize().background(ScrimGradient))
        }

        overlay(hovered)
    }
}

/** Bottom-aligned row of quick actions that fades in on hover. */
@Composable
fun BoxScope.CardHoverActions(
    visible: Boolean,
    content: @Composable RowScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(140)),
        exit = fadeOut(tween(140)),
        modifier = Modifier.align(Alignment.BottomEnd)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

/** Small translucent circular action used inside a card's hover overlay. */
@Composable
fun CardActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    tint: Color = Color.White
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    DesktopTooltip(text = contentDescription) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.Black.copy(alpha = if (hovered) 0.72f else 0.5f))
                .desktopClickable(interaction = interaction, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
