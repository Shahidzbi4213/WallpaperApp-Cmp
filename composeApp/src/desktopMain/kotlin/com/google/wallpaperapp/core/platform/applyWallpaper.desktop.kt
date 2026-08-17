package com.google.wallpaperapp.core.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import com.google.wallpaperapp.utils.WallpaperType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Desktop has no wallpaper API, so we write the image out and ask the desktop environment to
 * pick it up. [WallpaperType] (home / lock / both) has no desktop meaning and is ignored -- the
 * desktop UI never offers the choice.
 *
 * Only the macOS path is verified. Windows and Linux are best-effort; on any failure, and on an
 * unrecognised OS, the file is still on disk and the caller reports where it went.
 */
actual class WallpaperManager actual constructor() {

    actual suspend fun applyWallpaper(
        image: ImageBitmap,
        type: WallpaperType
    ): WallpaperApplyResult = withContext(Dispatchers.IO) {
        val file = runCatching { image.writeToDisk() }
            .getOrElse { return@withContext WallpaperApplyResult.Failure(it.message) }

        val command = wallpaperCommand(file) ?: return@withContext manualFallback(file)

        runCatching { runCommand(command) }
            .fold(
                onSuccess = { ok -> if (ok) WallpaperApplyResult.Success else manualFallback(file) },
                onFailure = { manualFallback(file) }
            )
    }
}

/**
 * Desktop-only entry point, used instead of [WallpaperManager.applyWallpaper] by the desktop UI.
 *
 * A desktop background wants the full-resolution original, not the preview that happens to be on
 * screen, and holding a 5000px ImageBitmap in memory just to re-encode it would be wasteful. So we
 * fetch the original straight to disk and point the desktop environment at it.
 */
suspend fun applyWallpaperFromUrl(url: String): WallpaperApplyResult =
    withContext(Dispatchers.IO) {
        val dir = File(appDataDir(), "wallpapers").apply { if (!exists()) mkdirs() }
        val target = File(dir, "current-${url.hashCode().toUInt()}.jpg")

        if (!target.exists()) {
            when (val result = WallpaperDownloader().downloadWallpaperTo(url, target)) {
                is DownloadResult.Failure ->
                    return@withContext WallpaperApplyResult.Failure(result.throwable.message)

                is DownloadResult.Success -> Unit
            }
        }

        applyWallpaperFile(target)
    }

/**
 * Sets an image that is already on disk -- used by the generated mesh gradients, which never
 * touch the network.
 */
suspend fun applyWallpaperFile(file: File): WallpaperApplyResult = withContext(Dispatchers.IO) {
    if (!file.exists()) return@withContext WallpaperApplyResult.Failure("File not found: $file")

    val command = wallpaperCommand(file) ?: return@withContext manualFallback(file)
    runCatching { runCommand(command) }.fold(
        onSuccess = { ok -> if (ok) WallpaperApplyResult.Success else manualFallback(file) },
        onFailure = { manualFallback(file) }
    )
}

private fun ImageBitmap.writeToDisk(): File {
    val encoded = Image.makeFromBitmap(asSkiaBitmap())
        .encodeToData(EncodedImageFormat.PNG)
        ?: error("Could not encode the wallpaper as PNG")

    val dir = File(appDataDir(), "wallpapers").apply { if (!exists()) mkdirs() }
    // Fixed name: macOS and GNOME both re-read the same path, and we avoid piling up copies.
    return File(dir, "current.png").apply { writeBytes(encoded.bytes) }
}

private fun wallpaperCommand(file: File): List<String>? {
    val path = file.absolutePath
    return when (currentOs) {
        DesktopOs.MAC -> listOf(
            "osascript", "-e",
            """tell application "System Events" to tell every desktop to set picture to "$path""""
        )

        DesktopOs.WINDOWS -> listOf(
            "powershell", "-NoProfile", "-Command",
            """
            Add-Type -TypeDefinition 'using System.Runtime.InteropServices;
            public class W { [DllImport("user32.dll", CharSet=CharSet.Unicode)]
            public static extern int SystemParametersInfo(int a,int b,string c,int d); }';
            [W]::SystemParametersInfo(20, 0, "$path", 3)
            """.trimIndent()
        )

        // GNOME only. KDE/XFCE/sway fall through to the manual fallback.
        DesktopOs.LINUX -> listOf(
            "gsettings", "set", "org.gnome.desktop.background", "picture-uri-dark", "file://$path"
        )

        DesktopOs.UNKNOWN -> null
    }
}

private fun runCommand(command: List<String>): Boolean {
    val process = ProcessBuilder(command)
        .redirectErrorStream(true)
        .start()
    if (!process.waitFor(15, TimeUnit.SECONDS)) {
        process.destroyForcibly()
        return false
    }
    return process.exitValue() == 0
}

private fun manualFallback(file: File) =
    WallpaperApplyResult.Failure("Saved to ${file.absolutePath} — set it as your wallpaper manually")
