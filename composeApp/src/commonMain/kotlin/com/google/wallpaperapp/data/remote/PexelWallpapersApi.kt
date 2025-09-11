package com.google.wallpaperapp.data.remote

import com.google.wallpaperapp.data.remote.models.WallpaperMainResponse

interface PexelWallpapersApi {

    suspend fun getWallpapers(page: Int): WallpaperMainResponse

    suspend fun searchWallpaper(page: Int, query: String): WallpaperMainResponse
}