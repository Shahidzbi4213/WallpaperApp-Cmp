package com.google.wallpaperapp.core.platform

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import java.io.File

actual class WallpaperDownloader actual constructor() : KoinComponent {

    actual suspend fun downloadWallpaper(url: String, fileName: String): DownloadResult =
        downloadWallpaperTo(url, File(downloadsDir(), fileName.sanitizedFileName()))

    /** Same fetch, explicit destination. Used by the desktop "set as wallpaper" path. */
    suspend fun downloadWallpaperTo(url: String, target: File): DownloadResult =
        withContext(Dispatchers.IO) {
            runCatching {
                val client = getKoin().get<HttpClient>()
                client.get(url).bodyAsChannel().writeTo(target)
                DownloadResult.Success(target.absolutePath)
            }.getOrElse {
                target.delete()   // never leave a half-written image behind
                DownloadResult.Failure(it)
            }
        }
}

private suspend fun ByteReadChannel.writeTo(target: File) {
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    target.outputStream().use { out ->
        while (true) {
            val read = readAvailable(buffer, 0, buffer.size)
            if (read == -1) break
            if (read > 0) out.write(buffer, 0, read)
        }
    }
}

/** Pexels urls arrive with query strings; strip anything that is not filename-safe. */
internal fun String.sanitizedFileName(): String {
    val base = substringBefore('?').substringAfterLast('/').ifBlank { "wallpaper" }
    val safe = base.replace(Regex("[^A-Za-z0-9._-]"), "_")
    return if (safe.contains('.')) safe else "$safe.jpg"
}
