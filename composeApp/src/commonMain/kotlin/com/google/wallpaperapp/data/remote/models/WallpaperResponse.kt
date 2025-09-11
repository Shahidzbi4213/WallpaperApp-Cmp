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
    @Transient var page: Int = 0
)