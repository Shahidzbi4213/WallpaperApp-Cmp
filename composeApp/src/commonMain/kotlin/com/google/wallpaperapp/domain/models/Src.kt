package com.google.wallpaperapp.domain.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Src(
    @SerialName("medium")
    val medium: String,
    @SerialName("portrait")
    val portrait: String,
    @SerialName("small")
    val small:String
)