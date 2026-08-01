package com.google.wallpaperapp.domain.models

/**
 * Every provider the app can pull wallpapers from.
 *
 * [key] prefixes the provider's own id to build a globally unique [Wallpaper.id],
 * since provider ids only need to be unique within their own catalogue.
 */
enum class WallpaperSource(
    val key: String,
    val displayName: String,
    val siteUrl: String
) {
    PEXELS("pexels", "Pexels", "https://www.pexels.com"),
    WALLHAVEN("wallhaven", "Wallhaven", "https://wallhaven.cc"),
    BING("bing", "Bing", "https://www.bing.com"),
    UNSPLASH("unsplash", "Unsplash", "https://unsplash.com");

    fun idOf(providerId: String) = "$key:$providerId"
}
