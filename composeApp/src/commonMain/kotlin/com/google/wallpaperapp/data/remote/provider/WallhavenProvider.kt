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
 * Wallhaven search. Needs no API key and allows 45 requests/minute anonymously,
 * which makes it the app's fallback source when every keyed provider is
 * unavailable.
 *
 * It is also the only provider that can filter server-side on aspect ratio and
 * minimum resolution, so it returns phone-shaped wallpapers rather than
 * landscape stock photography.
 */
class WallhavenProvider(private val client: HttpClient) : WallpaperProvider {

    override val source = WallpaperSource.WALLHAVEN

    override suspend fun featured(page: Int): ProviderPage = request(page, query = null)

    override suspend fun search(query: String, page: Int): ProviderPage = request(page, query)

    private suspend fun request(page: Int, query: String?): ProviderPage =
        client.get("$BASE_URL/search") {
            // General category only, and SFW only. Both are deliberately fixed:
            // `purity` is the switch that would let NSFW content into the feed.
            parameter("categories", "100")
            parameter("purity", "100")
            parameter("atleast", "1080x1920")
            parameter("ratios", "9x16,9x18,9x20,10x16")
            parameter("page", page)
            if (query.isNullOrBlank()) {
                parameter("sorting", "toplist")
                parameter("topRange", "1M")
            } else {
                parameter("q", query)
                parameter("sorting", "relevance")
            }
        }.body<WallhavenListDto>().toProviderPage(query)

    private companion object {
        const val BASE_URL = "https://wallhaven.cc/api/v1"
    }
}

@Serializable
internal data class WallhavenListDto(
    val data: List<WallhavenWallpaperDto> = emptyList(),
    val meta: WallhavenMetaDto = WallhavenMetaDto(),
)

@Serializable
internal data class WallhavenMetaDto(
    @SerialName("current_page") val currentPage: Int = 1,
    @SerialName("last_page") val lastPage: Int = 1,
)

@Serializable
internal data class WallhavenWallpaperDto(
    val id: String,
    val url: String = "",
    val path: String = "",
    val category: String = "",
    @SerialName("dimension_x") val width: Int = 0,
    @SerialName("dimension_y") val height: Int = 0,
    val thumbs: WallhavenThumbsDto = WallhavenThumbsDto(),
)

@Serializable
internal data class WallhavenThumbsDto(
    val small: String = "",
    val large: String = "",
    val original: String = "",
)

internal fun WallhavenListDto.toProviderPage(query: String?) = ProviderPage(
    wallpapers = data.map { it.toWallpaper(query) },
    hasMore = meta.currentPage < meta.lastPage,
)

internal fun WallhavenWallpaperDto.toWallpaper(query: String?) = Wallpaper(
    id = WallpaperSource.WALLHAVEN.idOf(id),
    source = WallpaperSource.WALLHAVEN,
    thumbUrl = thumbs.small.ifBlank { thumbs.large },
    previewUrl = thumbs.large.ifBlank { path },
    fullUrl = path,
    width = width,
    height = height,
    // Wallhaven's search listing carries no tags (only the single-wallpaper
    // endpoint does), so fall back to what we asked for. This keeps the
    // "similar wallpapers" query on the detail screen meaningful.
    alt = query?.takeIf { it.isNotBlank() } ?: category,
    sourcePageUrl = url,
)
