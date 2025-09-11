package com.google.wallpaperapp.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WallpaperResponse(
    @SerialName("next_page")
    val nextPage: String? = null,
    @SerialName("page")
    val page: Int,
    @SerialName("per_page")
    val perPage: Int,
    @SerialName("photos") val wallpapers: List<Wallpaper>,
    @SerialName("prev_page") val prevPage: String? = null,
    @SerialName("total_results") val totalResults: Int
)

