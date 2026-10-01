@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package com.google.wallpaperapp.core.platform

import kotlin.js.Promise
import kotlinx.coroutines.await

@JsFun("""
(url, filename) => {
    return fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('Failed to fetch image: ' + response.statusText);
            return response.blob();
        })
        .then(blob => {
            const blobUrl = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = blobUrl;
            a.download = filename;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            setTimeout(() => URL.revokeObjectURL(blobUrl), 60000);
            return null;
        });
}
""")
private external fun jsDownloadFile(url: String, filename: String): Promise<JsAny?>

actual class WallpaperDownloader actual constructor() {
    actual suspend fun downloadWallpaper(url: String, fileName: String): DownloadResult {
        return try {
            jsDownloadFile(url, fileName).await()
            DownloadResult.Success(fileName)
        } catch (e: Throwable) {
            DownloadResult.Failure(e)
        }
    }
}

