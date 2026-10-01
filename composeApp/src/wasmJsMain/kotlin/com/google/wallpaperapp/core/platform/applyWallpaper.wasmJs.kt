package com.google.wallpaperapp.core.platform

import androidx.compose.ui.graphics.ImageBitmap
import com.google.wallpaperapp.utils.WallpaperType

actual class WallpaperManager actual constructor() {
    actual suspend fun applyWallpaper(
        image: ImageBitmap,
        type: WallpaperType
    ): WallpaperApplyResult {
        return WallpaperApplyResult.Success
    }
}

actual suspend fun applyWallpaperFromUrl(url: String): WallpaperApplyResult {
    val name = "screeny-${url.hashCode().toUInt()}.jpg"
    return when (val result = WallpaperDownloader().downloadWallpaper(url, name)) {
        is DownloadResult.Success -> WallpaperApplyResult.Success
        is DownloadResult.Failure -> WallpaperApplyResult.Failure(result.throwable.message)
    }
}
