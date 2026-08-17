package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.theme.TextHi
import com.google.wallpaperapp.ui.theme.glass

/**
 * Hover tooltip for icon-only controls. On desktop an unlabelled icon is a guessing game;
 * a tooltip is the platform's answer, and there is no touch equivalent to fall back on.
 */
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun DesktopTooltip(
    text: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    TooltipArea(
        modifier = modifier,
        delayMillis = 500,
        tooltip = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = TextHi,
                modifier = Modifier
                    .glass(RoundedCornerShape(6.dp), strong = true)
                    .padding(horizontal = 8.dp, vertical = 5.dp)
            )
        },
        content = content
    )
}
