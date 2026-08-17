package com.google.wallpaperapp.ui.desktop.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.focusable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.fullUrl
import com.google.wallpaperapp.ui.desktop.components.DesktopIconButton
import com.google.wallpaperapp.ui.desktop.components.DesktopTooltip
import com.google.wallpaperapp.ui.desktop.components.desktopClickable
import com.google.wallpaperapp.ui.theme.Crimson
import com.google.wallpaperapp.ui.theme.Ember
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
import wallpaperapp.composeapp.generated.resources.desktop_next_wallpaper
import wallpaperapp.composeapp.generated.resources.desktop_open_photographer
import wallpaperapp.composeapp.generated.resources.desktop_photographer
import wallpaperapp.composeapp.generated.resources.desktop_previous_wallpaper
import wallpaperapp.composeapp.generated.resources.desktop_remove_favourite
import wallpaperapp.composeapp.generated.resources.desktop_set_as_wallpaper
import wallpaperapp.composeapp.generated.resources.desktop_toggle_controls
import wallpaperapp.composeapp.generated.resources.desktop_wallpaper
import wallpaperapp.composeapp.generated.resources.download

/**
 * Full-screen high-quality wallpaper preview viewer for desktop.
 *
 * Displays the original high-resolution photo with an interactive glassmorphic HUD.
 * Clicking anywhere on the canvas or pressing Space toggles the info/action overlays.
 * Left/Right keys or buttons flip through the current list of wallpapers.
 */
@Composable
fun DesktopFullScreenViewer(
    wallpaper: Wallpaper,
    isFavourite: Boolean,
    items: List<Wallpaper>,
    onClose: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onApply: (Wallpaper) -> Unit,
    onDownload: (Wallpaper) -> Unit,
    onToggleFavourite: (Wallpaper) -> Unit,
    onCopyLink: (Wallpaper) -> Unit,
    onOpenPhotographer: (Wallpaper) -> Unit,
    modifier: Modifier = Modifier
) {
    var showControls by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }

    val currentIndex = remember(wallpaper.id, items) {
        items.indexOfFirst { it.id == wallpaper.id }
    }
    val hasPrev = currentIndex > 0
    val hasNext = currentIndex != -1 && currentIndex < items.lastIndex

    LaunchedEffect(Unit) {
        runCatching { focusRequester.requestFocus() }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Ink950.copy(alpha = 0.94f))
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.Escape -> {
                            onClose()
                            true
                        }
                        Key.DirectionLeft -> {
                            if (hasPrev) onPrevious()
                            true
                        }
                        Key.DirectionRight -> {
                            if (hasNext) onNext()
                            true
                        }
                        Key.Spacebar -> {
                            showControls = !showControls
                            true
                        }
                        Key.F -> {
                            onToggleFavourite(wallpaper)
                            true
                        }
                        else -> false
                    }
                } else false
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { showControls = !showControls }
            )
    ) {
        // High-resolution image canvas
        CoilImage(
            imageModel = { wallpaper.fullUrl },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Fit,
                contentDescription = wallpaper.alt.ifBlank { null },
                alignment = Alignment.Center
            ),
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Ember,
                        strokeWidth = 2.5.dp,
                        modifier = Modifier.size(40.dp)
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Top Navigation & Title Bar
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(tween(180)) + slideInVertically(tween(180)) { -it },
            exit = fadeOut(tween(180)) + slideOutVertically(tween(180)) { -it },
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.65f))
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DesktopIconButton(
                    icon = Icons.Outlined.Close,
                    contentDescription = stringResource(Res.string.desktop_close_preview),
                    onClick = onClose
                )

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = wallpaper.alt.ifBlank { stringResource(Res.string.desktop_wallpaper) },
                        style = MaterialTheme.typography.titleMedium,
                        color = TextHi,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (items.isNotEmpty() && currentIndex != -1) {
                        Text(
                            text = "${currentIndex + 1} / ${items.size}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextLow
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Text(
                    text = stringResource(Res.string.desktop_toggle_controls),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextLow.copy(alpha = 0.7f),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        // Left Navigation Chevron
        if (hasPrev) {
            AnimatedVisibility(
                visible = showControls,
                enter = fadeIn(tween(180)),
                exit = fadeOut(tween(180)),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp)
            ) {
                FloatingNavButton(
                    icon = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = stringResource(Res.string.desktop_previous_wallpaper),
                    onClick = onPrevious
                )
            }
        }

        // Right Navigation Chevron
        if (hasNext) {
            AnimatedVisibility(
                visible = showControls,
                enter = fadeIn(tween(180)),
                exit = fadeOut(tween(180)),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 24.dp)
            ) {
                FloatingNavButton(
                    icon = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = stringResource(Res.string.desktop_next_wallpaper),
                    onClick = onNext
                )
            }
        }

        // Bottom HUD / Details & Actions Island
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(tween(180)) + slideInVertically(tween(180)) { it },
            exit = fadeOut(tween(180)) + slideOutVertically(tween(180)) { it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp, start = 24.dp, end = 24.dp)
        ) {
            val pillShape = RoundedCornerShape(16.dp)
            Box(
                modifier = Modifier
                    .glass(pillShape, strong = true)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    if (wallpaper.photographerName.isNotBlank()) {
                        Column {
                            Text(
                                text = stringResource(Res.string.desktop_photographer),
                                style = eyebrowStyle(),
                                color = TextLow,
                                fontSize = 10.sp
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = wallpaper.photographerName,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = TextHi,
                                    maxLines = 1
                                )
                                if (wallpaper.photographerUrl.isNotBlank()) {
                                    DesktopTooltip(text = stringResource(Res.string.desktop_open_photographer)) {
                                        Icon(
                                            Icons.AutoMirrored.Outlined.OpenInNew,
                                            contentDescription = stringResource(Res.string.desktop_open_photographer),
                                            tint = TextMid,
                                            modifier = Modifier
                                                .size(15.dp)
                                                .desktopClickable { onOpenPhotographer(wallpaper) }
                                        )
                                    }
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .width(1.dp)
                                .background(Color.White.copy(alpha = 0.15f))
                        )
                    }

                    // Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Primary Set Wallpaper Action
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(EmberGradient)
                                .desktopClickable { onApply(wallpaper) }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.Wallpaper,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = stringResource(Res.string.desktop_set_as_wallpaper),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                        }

                        // Download Action
                        DesktopViewerActionButton(
                            icon = Icons.Outlined.Download,
                            contentDescription = stringResource(Res.string.download),
                            onClick = { onDownload(wallpaper) }
                        )

                        // Favourite Action
                        DesktopViewerActionButton(
                            icon = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isFavourite) {
                                stringResource(Res.string.desktop_remove_favourite)
                            } else {
                                stringResource(Res.string.desktop_add_favourite)
                            },
                            tint = if (isFavourite) Crimson else TextHi,
                            onClick = { onToggleFavourite(wallpaper) }
                        )

                        // Copy Link Action
                        DesktopViewerActionButton(
                            icon = Icons.Outlined.Link,
                            contentDescription = stringResource(Res.string.desktop_copy_link),
                            onClick = { onCopyLink(wallpaper) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingNavButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    DesktopTooltip(text = contentDescription) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .glass(CircleShape, strong = true)
                .desktopClickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun DesktopViewerActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    tint: Color = TextHi
) {
    DesktopTooltip(text = contentDescription) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .glass(RoundedCornerShape(10.dp), strong = true)
                .desktopClickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
