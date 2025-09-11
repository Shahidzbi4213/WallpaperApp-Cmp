package com.google.wallpaperapp.domain.models

data class Wallpaper (
    val id: Long,
    val photographerName: String,
    val photographerUrl: String,
    val medium: String,
    val portrait: String,
    val small:String
)