package com.google.wallpaperapp.domain.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Immutable
@Serializable
data class Wallpaper(
    @SerialName("id")
    val id: Long,
    @SerialName("photographer")
    val photographerName: String,
    @SerialName("photographer_url")
    val photographerUrl: String,
    @SerialName("src")
     val wallpaperSource: Src,
    @Transient var page: Int = 0
)