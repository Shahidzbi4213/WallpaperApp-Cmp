package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.gridUrl
import com.google.wallpaperapp.ui.composables.LazyPagingItems
import com.google.wallpaperapp.ui.theme.Crimson
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_add_favourite
import wallpaperapp.composeapp.generated.resources.desktop_copy_link
import wallpaperapp.composeapp.generated.resources.desktop_load_error
import wallpaperapp.composeapp.generated.resources.desktop_load_more
import wallpaperapp.composeapp.generated.resources.desktop_photographer
import wallpaperapp.composeapp.generated.resources.desktop_remove_favourite
import wallpaperapp.composeapp.generated.resources.desktop_set_as_wallpaper
import wallpaperapp.composeapp.generated.resources.download

/**
 * The one grid every browsing screen uses. Handles the paging load states so Home, a category,
 * search results and similar-wallpapers don't each reimplement skeletons, errors and empty states.
 */
@Composable
fun WallpaperPagedGrid(
    items: LazyPagingItems<Wallpaper>,
    favouriteIds: Set<Long>,
    onOpen: (Wallpaper) -> Unit,
    onToggleFavourite: (Wallpaper) -> Unit,
    onApply: (Wallpaper) -> Unit,
    onDownload: (Wallpaper) -> Unit,
    onCopyUrl: (Wallpaper) -> Unit,
    onOpenPhotographer: (Wallpaper) -> Unit,
    emptyTitle: String,
    emptySubtitle: String,
    modifier: Modifier = Modifier,
    selectedId: Long? = null,
    state: LazyGridState = rememberLazyGridState()
) {
    val applyLabel = stringResource(Res.string.desktop_set_as_wallpaper)
    val downloadLabel = stringResource(Res.string.download)
    val addFavLabel = stringResource(Res.string.desktop_add_favourite)
    val removeFavLabel = stringResource(Res.string.desktop_remove_favourite)
    val copyLinkLabel = stringResource(Res.string.desktop_copy_link)
    val photographerLabel = stringResource(Res.string.desktop_photographer)

    val refresh = items.loadState.refresh

    when {
        refresh is LoadState.Error -> {
            DesktopErrorState(
                message = refresh.error.message ?: stringResource(Res.string.desktop_load_error),
                onRetry = { items.retry() },
                modifier = modifier
            )
            return
        }

        refresh is LoadState.Loading && items.itemCount == 0 -> {
            // Skeleton grid rather than one spinner: a desktop window shows a dozen cards at
            // once, so a lone spinner in the middle reads as a hang.
            ScrollableGrid(modifier = modifier) {
                items(count = 12) { WallpaperCardSkeleton() }
            }
            return
        }

        items.itemCount == 0 -> {
            DesktopEmptyState(title = emptyTitle, subtitle = emptySubtitle, modifier = modifier)
            return
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        ScrollableGrid(state = state) {
            items(count = items.itemCount) { index ->
                val wallpaper = items[index]
                if (wallpaper == null) {
                    WallpaperCardSkeleton()
                } else {
                    val isFavourite = wallpaper.id in favouriteIds

                    ContextMenuArea(items = {
                        listOf(
                            ContextMenuItem(applyLabel) { onApply(wallpaper) },
                            ContextMenuItem(downloadLabel) { onDownload(wallpaper) },
                            ContextMenuItem(
                                if (isFavourite) removeFavLabel else addFavLabel
                            ) { onToggleFavourite(wallpaper) },
                            ContextMenuItem(copyLinkLabel) { onCopyUrl(wallpaper) },
                            ContextMenuItem("$photographerLabel: ${wallpaper.photographerName}") {
                                onOpenPhotographer(wallpaper)
                            },
                        )
                    }) {
                        WallpaperCard(
                            imageUrl = wallpaper.gridUrl,
                            contentDescription = wallpaper.alt.ifBlank { null },
                            selected = wallpaper.id == selectedId,
                            onClick = { onOpen(wallpaper) }
                        ) { hovered ->
                            CardHoverActions(visible = hovered) {
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
                                    icon = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = if (isFavourite) removeFavLabel else addFavLabel,
                                    tint = if (isFavourite) Crimson else androidx.compose.ui.graphics.Color.White,
                                    onClick = { onToggleFavourite(wallpaper) }
                                )
                            }
                        }
                    }
                }
            }

            if (items.loadState.append is LoadState.Loading) {
                items(count = 3) { WallpaperCardSkeleton() }
            }

            if (items.loadState.append is LoadState.Error) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    DesktopTextButton(text = stringResource(Res.string.desktop_load_more), onClick = { items.retry() })
                }
            }
        }
    }
}
