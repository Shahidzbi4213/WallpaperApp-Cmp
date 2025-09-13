package com.google.wallpaperapp.core.platform

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.component.KoinComponent
import kotlin.coroutines.resume

actual class WallpaperDownloader : KoinComponent {

    actual suspend fun downloadWallpaper(url: String, fileName: String): DownloadResult {
        return try {

            val context = getKoin().get<Context>()
            val request = DownloadManager.Request(url.toUri()).apply {
                setTitle("Downloading wallpaper")
                setDescription(fileName)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
            }

            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = dm.enqueue(request)

            suspendCancellableCoroutine { cont ->
                cont.resume(DownloadResult.Success("Pictures/$fileName"))
            }
        } catch (t: Throwable) {
            DownloadResult.Failure(t)
        }
    }
}
