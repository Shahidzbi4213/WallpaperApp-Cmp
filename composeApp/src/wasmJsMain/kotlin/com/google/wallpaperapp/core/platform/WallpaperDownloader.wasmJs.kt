package com.google.wallpaperapp.core.platform

@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
@JsFun("(url, filename) => { const a = document.createElement('a'); a.href = url; a.download = filename; a.target = '_blank'; document.body.appendChild(a); a.click(); document.body.removeChild(a); }")
private external fun jsDownloadFile(url: String, filename: String)

actual class WallpaperDownloader actual constructor() {
    actual suspend fun downloadWallpaper(url: String, fileName: String): DownloadResult {
        return try {
            jsDownloadFile(url, fileName)
            DownloadResult.Success(fileName)
        } catch (e: Throwable) {
            DownloadResult.Failure(e)
        }
    }
}
