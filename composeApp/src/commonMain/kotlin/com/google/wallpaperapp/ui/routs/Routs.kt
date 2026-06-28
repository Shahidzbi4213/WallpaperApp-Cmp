package com.google.wallpaperapp.ui.routs

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routs : NavKey {

    @Serializable
    data object Splash : Routs, NavKey

    @Serializable
    data object MainScreen : Routs

    @Serializable
    data class FavouriteDetail(val wallpaperId: Long, val wallpaperUrl: String) : Routs

    @Serializable
    @Stable
    data class CategoryDetail(val categoryName: String) : Routs

    @Serializable
    data object SearchedWallpaper : Routs

    @Serializable
    data class WallpaperDetail(val wallpaperId: Long) : Routs


    @Serializable
    data object Language : Routs


}