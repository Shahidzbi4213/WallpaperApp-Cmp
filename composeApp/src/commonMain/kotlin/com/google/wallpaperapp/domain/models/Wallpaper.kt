package com.google.wallpaperapp.domain.models

import com.google.wallpaperapp.core.platform.PlatformType
import com.google.wallpaperapp.core.platform.getPlatformType

data class Wallpaper (
    val id: Long,
    val photographerName: String,
    val photographerUrl: String,
    val medium: String,
    val portrait: String,
    val small:String,
    val landscape: String = "",
    val original: String = "",
    val alt: String = ""
)

/**
 * The url to show in a grid or preview. Desktop wants the 16:9 crop, phones want the tall one.
 * Falls back to portrait for rows cached before the landscape column existed.
 */
val Wallpaper.gridUrl: String
    get() = if (getPlatformType() == PlatformType.DESKTOP) landscape.ifBlank { portrait } else portrait

/** Highest resolution available -- used when downloading or setting an actual wallpaper. */
val Wallpaper.fullUrl: String
    get() = original.ifBlank { landscape.ifBlank { portrait } }
