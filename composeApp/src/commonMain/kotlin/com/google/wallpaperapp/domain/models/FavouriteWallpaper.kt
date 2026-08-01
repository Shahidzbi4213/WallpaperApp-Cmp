package com.google.wallpaperapp.domain.models

data class FavouriteWallpaper(
    val id: String,
    /** Preview URL, shown in the favourites grid. */
    val wallpaper: String,
    val source: WallpaperSource? = null,
    val fullUrl: String = "",
    val authorName: String = "",
    val authorUrl: String = "",
    val sourcePageUrl: String = "",
)
