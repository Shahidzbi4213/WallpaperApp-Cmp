package com.google.wallpaperapp.data.remote

import com.google.wallpaperapp.domain.models.WallpaperResponse

interface PexelWallpapersApi {

    suspend fun getWallpapers(page: Int): WallpaperResponse

    suspend fun searchWallpaper(page: Int, query: String): WallpaperResponse
}