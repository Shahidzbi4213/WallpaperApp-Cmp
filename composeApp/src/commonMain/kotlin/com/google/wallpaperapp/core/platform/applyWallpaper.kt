package com.google.wallpaperapp.core.platform

import androidx.compose.ui.graphics.ImageBitmap
import com.google.wallpaperapp.utils.WallpaperType


sealed interface WallpaperApplyResult {
    object Success : WallpaperApplyResult
    data class Failure(val message: String? = null) : WallpaperApplyResult
}


expect class WallpaperManager(){

     suspend fun applyWallpaper(
        image: ImageBitmap,
        type: WallpaperType
    ): WallpaperApplyResult
}
