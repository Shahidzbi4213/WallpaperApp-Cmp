package com.google.wallpaperapp.data.remote.provider

import com.google.wallpaperapp.data.remote.ApiKeys
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.WallpaperSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Pexels curated feed and search.
 *
 * Auth is attached per request rather than on the shared client, so the key is
 * never sent to any other provider's host.
 */
class PexelsProvider(
    private val client: HttpClient,
    private val apiKey: String = ApiKeys.PEXELS,
) : WallpaperProvider {

    override val source = WallpaperSource.PEXELS

    override val isEnabled: Boolean get() = apiKey.isNotBlank()

    override suspend fun featured(page: Int): ProviderPage =
        client.get("$BASE_URL/curated") {
            header(HttpHeaders.Authorization, apiKey)
            parameter("page", page)
            parameter("per_page", PAGE_SIZE)
        }.body<PexelsListDto>().toProviderPage()

    override suspend fun search(query: String, page: Int): ProviderPage =
        client.get("$BASE_URL/search") {
            header(HttpHeaders.Authorization, apiKey)
            parameter("query", query)
            parameter("orientation", "portrait")
            parameter("page", page)
            parameter("per_page", PAGE_SIZE)
        }.body<PexelsListDto>().toProviderPage()

    private companion object {
        const val BASE_URL = "https://api.pexels.com/v1"
        const val PAGE_SIZE = 24
    }
}

@Serializable
internal data class PexelsListDto(
    @SerialName("photos") val photos: List<PexelsPhotoDto> = emptyList(),
    @SerialName("next_page") val nextPage: String? = null,
)

@Serializable
internal data class PexelsPhotoDto(
    val id: Long,
    val width: Int = 0,
    val height: Int = 0,
    val url: String = "",
    val alt: String = "",
    @SerialName("photographer") val photographer: String = "",
    @SerialName("photographer_url") val photographerUrl: String = "",
    @SerialName("src") val src: PexelsSrcDto,
)

@Serializable
internal data class PexelsSrcDto(
    val original: String = "",
    val large: String = "",
    val medium: String = "",
    val portrait: String = "",
    val small: String = "",
)

internal fun PexelsListDto.toProviderPage() = ProviderPage(
    wallpapers = photos.map { it.toWallpaper() },
    hasMore = nextPage != null,
)

internal fun PexelsPhotoDto.toWallpaper() = Wallpaper(
    id = WallpaperSource.PEXELS.idOf(id.toString()),
    source = WallpaperSource.PEXELS,
    thumbUrl = src.medium.ifBlank { src.small },
    previewUrl = src.portrait.ifBlank { src.large },
    // `original` is what the user actually wants on their home screen; the old
    // code downloaded the `portrait` derivative instead.
    fullUrl = src.original.ifBlank { src.portrait },
    width = width,
    height = height,
    alt = alt,
    authorName = photographer,
    authorUrl = photographerUrl,
    sourcePageUrl = url,
)
