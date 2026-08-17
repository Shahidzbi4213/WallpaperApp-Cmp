package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.gridUrl
import com.google.wallpaperapp.ui.desktop.paging.PageState
import com.google.wallpaperapp.ui.desktop.paging.DESKTOP_PER_PAGE
import com.google.wallpaperapp.ui.theme.Crimson
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_add_favourite
import wallpaperapp.composeapp.generated.resources.desktop_copy_link
import wallpaperapp.composeapp.generated.resources.desktop_full_screen_preview
import wallpaperapp.composeapp.generated.resources.desktop_photographer
import wallpaperapp.composeapp.generated.resources.desktop_remove_favourite
import wallpaperapp.composeapp.generated.resources.desktop_set_as_wallpaper
import wallpaperapp.composeapp.generated.resources.download

/**
 * A single numbered page of wallpapers plus its pagination bar. Replaces the infinite-scroll
 * grid on desktop: the page is a fixed, knowable unit rather than a list that never ends.
 */
@Composable
fun WallpaperPageGrid(
    state: PageState,
    favouriteIds: Set<Long>,
    onPageSelected: (Int) -> Unit,
    onRetry: () -> Unit,
    onOpen: (Wallpaper) -> Unit,
    onOpenFullScreen: (Wallpaper) -> Unit,
    onToggleFavourite: (Wallpaper) -> Unit,
    onApply: (Wallpaper) -> Unit,
    onDownload: (Wallpaper) -> Unit,
    onCopyUrl: (Wallpaper) -> Unit,
    onOpenPhotographer: (Wallpaper) -> Unit,
    emptyTitle: String,
    emptySubtitle: String,
    modifier: Modifier = Modifier,
    selectedId: Long? = null
) {
    val applyLabel = stringResource(Res.string.desktop_set_as_wallpaper)
    val downloadLabel = stringResource(Res.string.download)
    val addFavLabel = stringResource(Res.string.desktop_add_favourite)
    val removeFavLabel = stringResource(Res.string.desktop_remove_favourite)
    val copyLinkLabel = stringResource(Res.string.desktop_copy_link)
    val photographerLabel = stringResource(Res.string.desktop_photographer)
    val fullScreenLabel = stringResource(Res.string.desktop_full_screen_preview)

    val gridState = rememberLazyGridState()

    // A new page starts at the top. Landing mid-scroll on page 5 is exactly the disorientation
    // numbered pages are meant to remove.
    LaunchedEffect(state.page, state.items.firstOrNull()?.id) {
        if (state.items.isNotEmpty()) gridState.scrollToItem(0)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when {
                state.error != null -> DesktopErrorState(
                    message = stringResource(state.error),
                    onRetry = onRetry
                )

                state.isLoading && state.items.isEmpty() -> ScrollableGrid(state = gridState) {
                    items(count = DESKTOP_PER_PAGE) { WallpaperCardSkeleton() }
                }

                state.isEmpty -> DesktopEmptyState(title = emptyTitle, subtitle = emptySubtitle)

                else -> AnimatedContent(
                    targetState = state.page,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally(tween(240)) { width -> width / 4 } + fadeIn(tween(200)))
                                .togetherWith(slideOutHorizontally(tween(200)) { width -> -width / 4 } + fadeOut(tween(160)))
                        } else {
                            (slideInHorizontally(tween(240)) { width -> -width / 4 } + fadeIn(tween(200)))
                                .togetherWith(slideOutHorizontally(tween(200)) { width -> width / 4 } + fadeOut(tween(160)))
                        }.using(SizeTransform(clip = false))
                    },
                    label = "pageTransition",
                    modifier = Modifier.fillMaxSize()
                ) { _ ->
                    ScrollableGrid(state = gridState) {
                        items(count = state.items.size) { index ->
                            val wallpaper = state.items[index]
                            val isFavourite = wallpaper.id in favouriteIds

                            ContextMenuArea(items = {
                                listOf(
                                    ContextMenuItem(fullScreenLabel) { onOpenFullScreen(wallpaper) },
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
                                            icon = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                            contentDescription = if (isFavourite) removeFavLabel else addFavLabel,
                                            tint = if (isFavourite) Crimson else Color.White,
                                            onClick = { onToggleFavourite(wallpaper) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        PaginationBar(
            currentPage = state.page,
            totalPages = state.totalPages,
            enabled = !state.isLoading,
            onPageSelected = onPageSelected
        )
    }
}
