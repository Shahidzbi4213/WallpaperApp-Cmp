package com.google.wallpaperapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * The core Screeny surface treatment: a translucent white fill + 1px hairline
 * border. Used for the bottom nav, sheets, search field, chips and icon
 * buttons — anything that floats over content.
 *
 * (True `backdrop-filter` blur isn't available in Compose; the translucent
 * fill over the dark app background is the faithful stand-in.)
 */
fun Modifier.glass(
    shape: Shape,
    strong: Boolean = false,
    borderHi: Boolean = false,
): Modifier = this
    .background(if (strong) GlassFillStrong else GlassFill, shape)
    .border(1.dp, if (borderHi) GlassBorderHi else GlassBorder, shape)
