package com.google.wallpaperapp.data.remote.provider

import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.WallpaperSource

/**
 * One wallpaper API. Implementations own their base URL, their auth, their wire
 * format and the mapping to [Wallpaper] — nothing provider-specific escapes this
 * package.
 */
interface WallpaperProvider {

    val source: WallpaperSource

    /** False when a required API key is missing, so the aggregator skips it. */
    val isEnabled: Boolean get() = true

    /** The provider's own feed, used for the home grid. */
    suspend fun featured(page: Int): ProviderPage

    suspend fun search(query: String, page: Int): ProviderPage
}

data class ProviderPage(
    val wallpapers: List<Wallpaper>,
    val hasMore: Boolean,
) {
    companion object {
        val Empty = ProviderPage(emptyList(), hasMore = false)
    }
}
