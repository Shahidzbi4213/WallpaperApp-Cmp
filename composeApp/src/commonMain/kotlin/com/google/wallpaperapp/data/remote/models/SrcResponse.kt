package com.google.wallpaperapp.data.remote.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class SrcResponse(
    @SerialName("medium")
    val medium: String,
    @SerialName("portrait")
    val portrait: String,
    @SerialName("small")
    val small: String,
    // Pexels always returns these two; they were previously dropped by ignoreUnknownKeys.
    // landscape (1200x627) backs the desktop grid, original backs apply/download.
    @SerialName("landscape")
    val landscape: String = "",
    @SerialName("original")
    val original: String = ""
)