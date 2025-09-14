package com.google.wallpaperapp.core.platform

import androidx.compose.ui.graphics.ImageBitmap
import com.google.wallpaperapp.utils.WallpaperType

actual class WallpaperManager actual constructor() {
    actual suspend fun applyWallpaper(image: ImageBitmap, type: WallpaperType): WallpaperApplyResult {
     return   WallpaperApplyResult.Success
    }
}