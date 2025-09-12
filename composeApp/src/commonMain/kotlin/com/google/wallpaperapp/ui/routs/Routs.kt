package com.google.wallpaperapp.ui.routs

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routs {

    @Serializable
    data object Splash : Routs

    @Serializable
    data object Home : Routs

    @Serializable
    data object Categories : Routs

    @Serializable
    data object Favourite : Routs

    @Serializable
    data class FavouriteDetail(val wallpaperId: Long , val wallpaperUrl: String) : Routs

    @Serializable
    data object Settings : Routs

    @Serializable
    @Stable
    data class CategoryDetail(val query: String) : Routs

    @Serializable
    data object SearchedWallpaper : Routs

    @Serializable
    data class WallpaperDetail(val wallpaperId: Long) : Routs


    @Serializable
    data object Language : Routs


}