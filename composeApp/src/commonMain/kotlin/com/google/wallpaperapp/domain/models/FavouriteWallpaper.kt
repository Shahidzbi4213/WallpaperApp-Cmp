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
    get() = when (getPlatformType()) {
        PlatformType.DESKTOP, PlatformType.WEB -> {
            val base = landscape.ifBlank { wallpaper }
            if (base.contains("images.pexels.com") && !base.contains("dpr=")) {
                "$base&dpr=2"
            } else if (base.contains("dpr=1")) {
                base.replace("dpr=1", "dpr=2")
            } else {
                base
            }
        }
        else -> wallpaper
    }
