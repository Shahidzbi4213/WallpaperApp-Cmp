package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.core.platform.openUrl
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.theme.glass

/**
 * Credits the photographer and the provider.
 *
 * Pexels and Unsplash both require visible attribution with a link back, and
 * until now the app parsed those fields but never showed them.
 */
@Composable
fun AttributionBar(
    wallpaper: Wallpaper,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)
    val author = wallpaper.authorName.trim()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .glass(shape)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Openverse-style providers hand back a ready-made licence line; use it
        // verbatim rather than reassembling one.
        val licence = wallpaper.attribution?.takeIf { it.isNotBlank() }
        if (licence != null) {
            Text(
                text = licence,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false),
            )
        } else if (author.isNotEmpty()) {
            Text(
                text = author,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                textDecoration = if (wallpaper.authorUrl.isNotBlank()) TextDecoration.Underline else null,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .clickable(enabled = wallpaper.authorUrl.isNotBlank()) {
                        openUrl(wallpaper.authorUrl)
                    },
            )
        }

        Text(
            text = "on ${wallpaper.source.displayName}",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.75f),
            textDecoration = TextDecoration.Underline,
            maxLines = 1,
            modifier = Modifier.clickable {
                openUrl(wallpaper.sourcePageUrl.ifBlank { wallpaper.source.siteUrl })
            },
        )
    }
}
