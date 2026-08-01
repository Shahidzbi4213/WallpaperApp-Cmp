package com.google.wallpaperapp.domain.models

/**
 * A wallpaper, normalised across every provider.
 *
 * The three URLs map onto how the app actually uses an image: [thumbUrl] for the
 * staggered grid tile, [previewUrl] for the full-screen pager, and [fullUrl] for
 * download and for applying to the home/lock screen.
 */
data class Wallpaper(
    val id: String,
    val source: WallpaperSource,
    val thumbUrl: String,
    val previewUrl: String,
    val fullUrl: String,
    val width: Int = 0,
    val height: Int = 0,
    /** Free-text description; drives the "similar wallpapers" query on the detail screen. */
    val alt: String = "",
    val authorName: String = "",
    val authorUrl: String = "",
    /** The photo's page on the provider's site, linked from the attribution bar. */
    val sourcePageUrl: String = "",
    /** Pre-formatted licence line, when the provider supplies one. */
    val attribution: String? = null,
    /** Unsplash requires a hit to this endpoint whenever the user downloads. */
    val downloadLocation: String? = null,
)
