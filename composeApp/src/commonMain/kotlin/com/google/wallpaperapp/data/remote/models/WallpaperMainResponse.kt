package com.google.wallpaperapp.data.remote.models

import kotlinx.serialization.SerialName

data class WallpaperMainResponse(
    @SerialName("next_page")
    val nextPage: String? = null,
    @SerialName("page")
    val page: Int,
    @SerialName("per_page")
    val perPage: Int,
    @SerialName("photos") val wallpapers: List<WallpaperResponse>,
    @SerialName("prev_page") val prevPage: String? = null,
    @SerialName("total_results") val totalResults: Int
)