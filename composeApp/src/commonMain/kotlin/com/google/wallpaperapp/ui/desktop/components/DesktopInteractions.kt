package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.runtime.remember

/**
 * Clickable with the two things a desktop pointer expects and [Modifier.clickable] alone does not
 * give you: a hand cursor, and a hover source the caller can observe for a hover state.
 * No ripple -- ripples are a touch idiom; desktop signals affordance through the cursor and hover.
 */
fun Modifier.desktopClickable(
    enabled: Boolean = true,
    interaction: MutableInteractionSource? = null,
    onClick: () -> Unit
): Modifier = composed {
    val source = interaction ?: remember { MutableInteractionSource() }
    this
        .hoverable(source, enabled)
        .pointerHoverIcon(if (enabled) PointerIcon.Hand else PointerIcon.Default)
        .clickable(
            interactionSource = source,
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
}
