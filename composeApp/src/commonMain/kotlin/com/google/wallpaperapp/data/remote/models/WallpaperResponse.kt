package com.google.wallpaperapp.data.remote.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Immutable
@Serializable
data class WallpaperResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("photographer")
    val photographerName: String,
    @SerialName("photographer_url")
    val photographerUrl: String,
    @SerialName("src")
     val wallpaperSource: SrcResponse,
    @SerialName("alt")
    val alt: String = "",
    @SerialName("width")
    val width: Int = 0,
    @SerialName("height")
    val height: Int = 0,
    @Transient var page: Int = 0
)