package com.google.wallpaperapp.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.google.wallpaperapp.ui.routs.Routs

@Composable
fun ManageBarVisibility(
    currentEntry: () -> NavKey?,
    showTopBar: (Boolean) -> Unit,
    showBottomBar: (Boolean) -> Unit,
) {
    currentEntry()?.let { entry ->
        when (entry) {
            is Routs.Splash,
            is Routs.CategoryDetail,
            is Routs.SearchedWallpaper,
            is Routs.WallpaperDetail,
            is Routs.FavouriteDetail,
            is Routs.Language -> {
                showTopBar(false)
                showBottomBar(false)
            }
            else -> {
                showTopBar(true)
                showBottomBar(true)
            }
        }
    }
}
