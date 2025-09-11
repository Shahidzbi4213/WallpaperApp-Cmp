package com.google.wallpaperapp.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.google.wallpaperapp.ui.routs.Routs

@Composable
fun ManageBarVisibility(
    currentEntry: () -> NavBackStackEntry?,
    showTopBar: (Boolean) -> Unit,
    showBottomBar: (Boolean) -> Unit,
) {
    currentEntry()?.let { entry ->

        val route = entry.destination.route?.substringBefore("/")
        when (route) {

            in arrayOf(
                Routs.Splash::class.qualifiedName,
                Routs.CategoryDetail::class.qualifiedName,
                Routs.SearchedWallpaper::class.qualifiedName,
                Routs.WallpaperDetail::class.qualifiedName,
                Routs.FavouriteDetail::class.qualifiedName,
                Routs.Language::class.qualifiedName

            ) -> {
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
