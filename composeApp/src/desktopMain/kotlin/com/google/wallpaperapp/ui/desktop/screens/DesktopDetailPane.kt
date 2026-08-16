package com.google.wallpaperapp.ui.desktop.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.gridUrl
import com.google.wallpaperapp.ui.composables.LazyPagingItems
import com.google.wallpaperapp.ui.desktop.components.DesktopIconButton
import com.google.wallpaperapp.ui.desktop.components.WallpaperCard
import com.google.wallpaperapp.ui.desktop.components.desktopClickable
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import com.google.wallpaperapp.ui.theme.Crimson
import com.google.wallpaperapp.ui.theme.EmberGradient
import com.google.wallpaperapp.ui.theme.Ink950
import com.google.wallpaperapp.ui.theme.TextHi
import com.google.wallpaperapp.ui.theme.TextLow
import com.google.wallpaperapp.ui.theme.TextMid
import com.google.wallpaperapp.ui.theme.eyebrowStyle
import com.google.wallpaperapp.ui.theme.glass
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_add_favourite
import wallpaperapp.composeapp.generated.resources.desktop_close_preview
import wallpaperapp.composeapp.generated.resources.desktop_copy_link
import wallpaperapp.composeapp.generated.resources.desktop_open_photographer
import wallpaperapp.composeapp.generated.resources.desktop_photographer
import wallpaperapp.composeapp.generated.resources.desktop_remove_favourite
import wallpaperapp.composeapp.generated.resources.desktop_set_as_wallpaper
import wallpaperapp.composeapp.generated.resources.desktop_similar
import wallpaperapp.composeapp.generated.resources.desktop_wallpaper
import wallpaperapp.composeapp.generated.resources.download

/**
 * The desktop preview. Not the phone's full-viewport pager: the image sits at its natural aspect
 * ratio inside a pane, with metadata and actions beside it, so the grid can stay visible on a
 * wide window and the user keeps browsing while previewing.
 */
@Composable
fun DesktopDetailPane(
    wallpaper: Wallpaper,
    isFavourite: Boolean,
    similar: LazyPagingItems<Wallpaper>,
    onClose: () -> Unit,
    onApply: (Wallpaper) -> Unit,
    onDownload: (Wallpaper) -> Unit,
    onToggleFavourite: (Wallpaper) -> Unit,
    onCopyLink: (Wallpaper) -> Unit,
    onOpenPhotographer: (Wallpaper) -> Unit,
    onOpenSimilar: (Wallpaper) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Ink950.copy(alpha = 0.72f))
            .padding(DesktopDimens.Gutter)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = wallpaper.alt.ifBlank { stringResource(Res.string.desktop_wallpaper) },
                style = MaterialTheme.typography.titleMedium,
                color = TextHi,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            DesktopIconButton(
                icon = Icons.Outlined.Close,
                contentDescription = stringResource(Res.string.desktop_close_preview),
                onClick = onClose
            )
        }

        Spacer(Modifier.height(14.dp))

        // Fit, not crop: the point of the preview is to see the whole image.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .heightIn(min = 200.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            CoilImage(
                imageModel = { wallpaper.gridUrl },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit,
                    contentDescription = wallpaper.alt.ifBlank { null },
                    alignment = Alignment.Center
                ),
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(16.dp))

        if (wallpaper.photographerName.isNotBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(Res.string.desktop_photographer), style = eyebrowStyle(), color = TextLow)
                    Text(
                        wallpaper.photographerName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMid
                    )
                }
                DesktopIconButton(
                    icon = Icons.Outlined.OpenInNew,
                    contentDescription = stringResource(Res.string.desktop_open_photographer),
                    onClick = { onOpenPhotographer(wallpaper) }
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        // No home/lock/both choice -- that distinction does not exist on a desktop.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PrimaryAction(
                label = stringResource(Res.string.desktop_set_as_wallpaper),
                onClick = { onApply(wallpaper) },
                modifier = Modifier.weight(1f)
            )
            SecondaryAction(
                icon = Icons.Outlined.Download,
                contentDescription = stringResource(Res.string.download),
                onClick = { onDownload(wallpaper) }
            )
            SecondaryAction(
                icon = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavourite) stringResource(Res.string.desktop_remove_favourite) else stringResource(Res.string.desktop_add_favourite),
                tint = if (isFavourite) Crimson else TextHi,
                onClick = { onToggleFavourite(wallpaper) }
            )
            SecondaryAction(
                icon = Icons.Outlined.Link,
                contentDescription = stringResource(Res.string.desktop_copy_link),
                onClick = { onCopyLink(wallpaper) }
            )
        }

        if (similar.itemCount > 0) {
            Spacer(Modifier.height(22.dp))
            Text(stringResource(Res.string.desktop_similar), style = eyebrowStyle(), color = TextLow)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(count = minOf(similar.itemCount, 12)) { index ->
                    val item = similar[index] ?: return@items
                    Box(modifier = Modifier.width(180.dp)) {
                        WallpaperCard(
                            imageUrl = item.gridUrl,
                            contentDescription = item.alt.ifBlank { null },
                            onClick = { onOpenSimilar(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PrimaryAction(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(42.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(EmberGradient)
            .desktopClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Outlined.Wallpaper,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(17.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.labelLarge, color = Color.White)
        }
    }
}

@Composable
private fun SecondaryAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    tint: Color = TextHi
) {
    val shape = RoundedCornerShape(9.dp)
    Box(
        modifier = Modifier
            .size(42.dp)
            .glass(shape, strong = true)
            .desktopClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(18.dp))
    }
}
