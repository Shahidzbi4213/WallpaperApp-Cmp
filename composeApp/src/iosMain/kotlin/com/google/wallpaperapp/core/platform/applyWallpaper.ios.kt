package com.google.wallpaperapp.core.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import com.google.wallpaperapp.utils.WallpaperType
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.skia.Data
import org.jetbrains.skia.Image
import org.jetbrains.skia.EncodedImageFormat
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class WallpaperManager actual constructor() {
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun applyWallpaper(image: ImageBitmap, type: WallpaperType): WallpaperApplyResult =
        suspendCancellableCoroutine { cont ->
            try {
                val uiImage = image.toUIImage()
                if (uiImage == null) {
                    cont.resumeWithException(IllegalArgumentException("Failed to convert ImageBitmap to UIImage"))
                    return@suspendCancellableCoroutine
                }

                dispatch_async(dispatch_get_main_queue()) {


                    val activityVC = UIActivityViewController(
                        activityItems = listOf(uiImage),
                        applicationActivities = null
                    )

                    // Set completion handler
                    activityVC.completionWithItemsHandler = { _, completed, _, error ->
                        when {
                            completed -> cont.resume(WallpaperApplyResult.Success)
                            error != null -> cont.resume(WallpaperApplyResult.Failure(error.localizedDescription))
                            else -> cont.resume(WallpaperApplyResult.Failure("User cancelled or unknown error"))
                        }
                    }

                    // Get the current view controller more reliably
                    val rootViewController = getCurrentViewController()
                    if (rootViewController != null) {
                        rootViewController.presentViewController(
                            activityVC,
                            animated = true,
                            completion = null
                        )
                    } else {
                        cont.resumeWithException(IllegalStateException("No view controller available to present share sheet"))
                    }
                }

            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }


    private fun getCurrentViewController(): UIViewController? {
        return try {
            UIApplication.sharedApplication.keyWindow?.rootViewController()
        } catch (e: Exception) {
            null
        }
    }

    private fun findTopViewController(viewController: UIViewController): UIViewController {
        return when {
            viewController.presentedViewController != null ->
                findTopViewController(viewController.presentedViewController!!)

            viewController is UINavigationController && viewController.topViewController != null ->
                findTopViewController(viewController.topViewController!!)

            viewController is UITabBarController && viewController.selectedViewController != null ->
                findTopViewController(viewController.selectedViewController!!)

            else -> viewController
        }
    }
}

fun ImageBitmap.toUIImage(): UIImage? {
    return try {
        val skiaImage = Image.makeFromBitmap(this.asSkiaBitmap())
        // Use a PNG format for better quality and compatibility
        val data = skiaImage.encodeToData(EncodedImageFormat.PNG) ?: return null
        UIImage(data = data.toNSData())
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun Data.toNSData(): NSData {
    val bytes = this.bytes
    return bytes.usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
    }
}