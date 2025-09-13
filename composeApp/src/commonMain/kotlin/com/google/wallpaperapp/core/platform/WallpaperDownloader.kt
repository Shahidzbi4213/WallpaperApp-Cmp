package com.google.wallpaperapp.core.platform

sealed class DownloadResult {
    data class Success(val filePath: String) : DownloadResult()
    data class Failure(val throwable: Throwable) : DownloadResult()
}

expect class WallpaperDownloader() {
    suspend fun downloadWallpaper(url: String, fileName: String): DownloadResult
}