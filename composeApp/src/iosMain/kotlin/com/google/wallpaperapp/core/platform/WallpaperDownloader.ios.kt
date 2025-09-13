package com.google.wallpaperapp.core.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.*
import platform.Photos.*
import platform.UIKit.UIImage
import kotlin.coroutines.resume

actual class WallpaperDownloader {

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun downloadWallpaper(url: String, fileName: String): DownloadResult {
        return suspendCancellableCoroutine { continuation ->
            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl == null) {
                continuation.resume(DownloadResult.Failure(Exception("Invalid Url"))) { cause, _, _ -> }
                return@suspendCancellableCoroutine
            }

            // Download image data
            val data = NSData.dataWithContentsOfURL(nsUrl)
            if (data == null) {
                continuation.resume(DownloadResult.Failure(Exception("Something went wrong"))) { cause, _, _ -> }
                return@suspendCancellableCoroutine
            }

            val image = UIImage.imageWithData(data)
            if (image == null) {
                continuation.resume(DownloadResult.Failure(Exception("Image is null"))) { cause, _, _ -> }
                return@suspendCancellableCoroutine
            }

            // Request Photo Library authorization
            PHPhotoLibrary.requestAuthorization { status ->
                when (status) {
                    PHAuthorizationStatusAuthorized,
                    PHAuthorizationStatusLimited -> {
                        PHPhotoLibrary.sharedPhotoLibrary().performChanges({
                            PHAssetChangeRequest.creationRequestForAssetFromImage(image)
                        }, completionHandler = { success, error ->
                            continuation.resume(DownloadResult.Success("Successfully Saved")) { cause, _, _ -> }
                        })
                    }

                    else -> {
                        continuation.resume(DownloadResult.Failure(Exception("Failed to save"))) { cause, _, _ -> }
                    }
                }
            }
        }
    }


}