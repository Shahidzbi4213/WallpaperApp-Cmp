package com.google.wallpaperapp.ui.desktop.screens

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.wallpaperapp.domain.models.FavouriteWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.gridUrl
import com.google.wallpaperapp.ui.desktop.components.CardActionButton
import com.google.wallpaperapp.ui.desktop.components.CardHoverActions
import com.google.wallpaperapp.ui.desktop.components.DesktopEmptyState
import com.google.wallpaperapp.ui.desktop.components.DesktopTextButton
import com.google.wallpaperapp.ui.desktop.components.ScrollableGrid
import com.google.wallpaperapp.ui.desktop.components.WallpaperCard
import com.google.wallpaperapp.ui.theme.Crimson
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_browse
import wallpaperapp.composeapp.generated.resources.desktop_full_screen_preview
import wallpaperapp.composeapp.generated.resources.desktop_no_favourites
import wallpaperapp.composeapp.generated.resources.desktop_no_favourites_sub
import wallpaperapp.composeapp.generated.resources.desktop_remove_favourite
import wallpaperapp.composeapp.generated.resources.desktop_set_as_wallpaper
import wallpaperapp.composeapp.generated.resources.download

@Composable
fun DesktopFavouriteScreen(
    favourites: List<FavouriteWallpaper>,
    onOpen: (Wallpaper) -> Unit,
    onOpenFullScreen: (Wallpaper) -> Unit,
    onRemove: (FavouriteWallpaper) -> Unit,
    onApply: (Wallpaper) -> Unit,
    onDownload: (Wallpaper) -> Unit,
    onExplore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val applyLabel = stringResource(Res.string.desktop_set_as_wallpaper)
    val downloadLabel = stringResource(Res.string.download)
    val removeLabel = stringResource(Res.string.desktop_remove_favourite)
    val fullScreenLabel = stringResource(Res.string.desktop_full_screen_preview)

    if (favourites.isEmpty()) {
        DesktopEmptyState(
            title = stringResource(Res.string.desktop_no_favourites),
            subtitle = stringResource(Res.string.desktop_no_favourites_sub),
            modifier = modifier,
            action = { DesktopTextButton(text = stringResource(Res.string.desktop_browse), onClick = onExplore) }
        )
        return
    }

    ScrollableGrid(modifier = modifier) {
        items(count = favourites.size) { index ->
            val favourite = favourites[index]
            // A favourite only stores urls, so rebuild the minimum Wallpaper the actions need.
            val wallpaper = favourite.asWallpaper()

            ContextMenuArea(items = {
                listOf(
                    ContextMenuItem(fullScreenLabel) { onOpenFullScreen(wallpaper) },
                    ContextMenuItem(applyLabel) { onApply(wallpaper) },
                    ContextMenuItem(downloadLabel) { onDownload(wallpaper) },
                    ContextMenuItem(removeLabel) { onRemove(favourite) },
                )
            }) {
                WallpaperCard(
                    imageUrl = favourite.gridUrl,
                    contentDescription = null,
                    onClick = { onOpen(wallpaper) }
                ) { hovered ->
                    CardHoverActions(visible = hovered) {
                        CardActionButton(
                            icon = Icons.Outlined.Fullscreen,
                            contentDescription = fullScreenLabel,
                            onClick = { onOpenFullScreen(wallpaper) }
                        )
                        CardActionButton(
                            icon = Icons.Outlined.Wallpaper,
                            contentDescription = applyLabel,
                            onClick = { onApply(wallpaper) }
                        )
                        CardActionButton(
                            icon = Icons.Outlined.Download,
                            contentDescription = downloadLabel,
                            onClick = { onDownload(wallpaper) }
                        )
                        CardActionButton(
                            icon = Icons.Filled.Favorite,
                            contentDescription = removeLabel,
                            tint = Crimson,
                            onClick = { onRemove(favourite) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Favourites persist urls only. The landscape column may be empty for rows saved on a phone, in
 * which case every accessor falls back to the portrait url it does have.
 */
internal fun FavouriteWallpaper.asWallpaper() = Wallpaper(
    id = id,
    photographerName = "",
    photographerUrl = "",
    medium = wallpaper,
    portrait = wallpaper,
    small = wallpaper,
    landscape = landscape,
    original = landscape.ifBlank { wallpaper }
)
