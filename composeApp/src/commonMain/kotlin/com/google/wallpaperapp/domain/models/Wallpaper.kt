package com.google.wallpaperapp.domain.models

import com.google.wallpaperapp.core.platform.PlatformType
import com.google.wallpaperapp.core.platform.getPlatformType

data class Wallpaper (
    val id: Long,
    val photographerName: String,
    val photographerUrl: String,
    val medium: String,
    val portrait: String,
    val small: String,
    val landscape: String = "",
    val original: String = "",
    val alt: String = "",
    val width: Int = 0,
    val height: Int = 0
)

/**
 * The url to show in a grid or preview. Desktop and web want the 16:9 crop at high-definition (HD),
 * phones want the tall one. Enhances Pexels landscape URLs with &dpr=2 for 2400px Retina/4K sharpness.
 */
val Wallpaper.gridUrl: String
    get() = when (getPlatformType()) {
        PlatformType.DESKTOP, PlatformType.WEB -> {
            val base = landscape.ifBlank { portrait }
            if (base.contains("images.pexels.com") && !base.contains("dpr=")) {
                "$base&dpr=2"
            } else if (base.contains("dpr=1")) {
                base.replace("dpr=1", "dpr=2")
            } else {
                base
            }
        }
        else -> portrait
    }

/** Highest resolution available -- used when downloading or setting an actual wallpaper. */
val Wallpaper.fullUrl: String
    get() = original.ifBlank { landscape.ifBlank { portrait } }
