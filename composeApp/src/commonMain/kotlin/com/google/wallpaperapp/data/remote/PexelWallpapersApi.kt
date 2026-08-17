package com.google.wallpaperapp.data.remote

import com.google.wallpaperapp.data.utils.Constant.PER_PAGE_ITEMS
import com.google.wallpaperapp.data.remote.models.WallpaperMainResponse

interface PexelWallpapersApi {

    suspend fun getWallpapers(page: Int, perPage: Int = PER_PAGE_ITEMS): WallpaperMainResponse

    suspend fun searchWallpaper(
        page: Int,
        query: String,
        perPage: Int = PER_PAGE_ITEMS
    ): WallpaperMainResponse
}
