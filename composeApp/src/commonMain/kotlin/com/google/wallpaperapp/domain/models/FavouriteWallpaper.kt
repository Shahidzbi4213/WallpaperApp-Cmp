package com.google.wallpaperapp.domain.models

import com.google.wallpaperapp.core.platform.PlatformType
import com.google.wallpaperapp.core.platform.getPlatformType

data class FavouriteWallpaper(
    val id: Long,
    val wallpaper: String,
    val landscape: String = ""
)

/** Favourites saved on a phone have no landscape url; show the portrait one rather than nothing. */
val FavouriteWallpaper.gridUrl: String
    get() = if (getPlatformType() == PlatformType.DESKTOP) landscape.ifBlank { wallpaper } else wallpaper
