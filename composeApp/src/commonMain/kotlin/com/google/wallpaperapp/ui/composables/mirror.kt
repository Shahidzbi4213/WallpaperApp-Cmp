package com.google.wallpaperapp.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@ReadOnlyComposable
    @Composable
    fun Modifier.mirror(): Modifier {
        return if (LocalLayoutDirection.current == LayoutDirection.Rtl)
          scale(scaleX = -1f, scaleY = 1f)
        else
            this
    }
