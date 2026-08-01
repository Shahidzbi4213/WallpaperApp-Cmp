package com.google.wallpaperapp.data.remote.provider

import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.WallpaperSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Bing's daily wallpaper archive.
 *
 * This endpoint is undocumented and unversioned — it has been stable for years,
 * but it carries no compatibility promise. The images are also copyrighted by
 * Microsoft and its partners and are personal-use only, so this provider is
 * gated behind [ENABLED]: flip that single flag to drop it entirely.
 *
 * It returns at most 8 images and does not paginate, so it seasons the feed
 * rather than carrying it.
 */
class BingDailyProvider(private val client: HttpClient) : WallpaperProvider {

    override val source = WallpaperSource.BING

    override val isEnabled: Boolean get() = ENABLED

    override suspend fun featured(page: Int): ProviderPage {
        if (page > 1) return ProviderPage.Empty
        return client.get(BASE_URL) {
            parameter("format", "js")
            parameter("idx", 0)
            parameter("n", MAX_IMAGES)
            parameter("mkt", "en-US")
        }.body<BingListDto>().toProviderPage()
    }

    /** Bing has no search endpoint; the daily set is all there is. */
    override suspend fun search(query: String, page: Int): ProviderPage = ProviderPage.Empty

    private companion object {
        const val ENABLED = true
        const val BASE_URL = "https://www.bing.com/HPImageArchive.aspx"
        const val MAX_IMAGES = 8
    }
}

@Serializable
internal data class BingListDto(
    val images: List<BingImageDto> = emptyList(),
)

@Serializable
internal data class BingImageDto(
    val urlbase: String = "",
    val copyright: String = "",
    val copyrightlink: String = "",
    val title: String = "",
    val hsh: String = "",
    val startdate: String = "",
)

internal fun BingListDto.toProviderPage() = ProviderPage(
    wallpapers = images.filter { it.urlbase.isNotBlank() }.map { it.toWallpaper() },
    // Single fixed-size batch, so pagination always ends here.
    hasMore = false,
)

internal fun BingImageDto.toWallpaper(): Wallpaper {
    val base = "https://www.bing.com$urlbase"
    return Wallpaper(
        id = WallpaperSource.BING.idOf(hsh.ifBlank { startdate }),
        source = WallpaperSource.BING,
        thumbUrl = "${base}_400x240.jpg",
        previewUrl = "${base}_1080x1920.jpg",
        fullUrl = "${base}_UHD.jpg",
        alt = title,
        // Bing packs the credit into `copyright`, e.g. "Lake Bled (© Jane/Getty)".
        authorName = copyright,
        sourcePageUrl = copyrightlink,
    )
}
