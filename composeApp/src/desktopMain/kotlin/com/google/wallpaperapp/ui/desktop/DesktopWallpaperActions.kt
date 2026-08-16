package com.google.wallpaperapp.ui.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import com.google.wallpaperapp.core.platform.DownloadResult
import com.google.wallpaperapp.core.platform.ToastManager
import com.google.wallpaperapp.core.platform.WallpaperApplyResult
import com.google.wallpaperapp.core.platform.WallpaperDownloader
import com.google.wallpaperapp.core.platform.applyWallpaperFromUrl
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.fullUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_browser_failed
import wallpaperapp.composeapp.generated.resources.desktop_download_failed
import wallpaperapp.composeapp.generated.resources.desktop_downloading
import wallpaperapp.composeapp.generated.resources.desktop_link_copied
import wallpaperapp.composeapp.generated.resources.desktop_saved_to
import wallpaperapp.composeapp.generated.resources.desktop_setting_wallpaper
import wallpaperapp.composeapp.generated.resources.desktop_wallpaper_failed
import wallpaperapp.composeapp.generated.resources.desktop_wallpaper_set

/**
 * The messages these actions emit. Resolved in composition and handed over, because the actions
 * themselves run in a coroutine long after the composable that started them has returned.
 */
@Immutable
data class WallpaperActionStrings(
    val settingWallpaper: String,
    val wallpaperSet: String,
    val wallpaperFailed: String,
    val downloading: String,
    val savedTo: String,
    val downloadFailed: String,
    val linkCopied: String,
    val browserFailed: String
)

/**
 * Apply / download / copy / open, in one place. Every desktop surface that can act on a wallpaper
 * -- grid hover buttons, the right-click menu, the detail pane -- routes through this, so the
 * behaviour and the messages stay identical across all of them.
 */
@Stable
class DesktopWallpaperActions(
    private val scope: CoroutineScope,
    private val clipboard: ClipboardManager,
    private val uriHandler: UriHandler,
    private val strings: WallpaperActionStrings
) {
    private val toast = ToastManager()

    fun apply(wallpaper: Wallpaper) {
        scope.launch {
            toast.showToast(strings.settingWallpaper)
            when (val result = applyWallpaperFromUrl(wallpaper.fullUrl)) {
                is WallpaperApplyResult.Success -> toast.showToast(strings.wallpaperSet)
                is WallpaperApplyResult.Failure ->
                    toast.showToast(result.message ?: strings.wallpaperFailed)
            }
        }
    }

    fun download(wallpaper: Wallpaper) {
        scope.launch {
            toast.showToast(strings.downloading)
            val name = "screeny-${wallpaper.id}.jpg"
            when (val result = WallpaperDownloader().downloadWallpaper(wallpaper.fullUrl, name)) {
                is DownloadResult.Success ->
                    toast.showToast(strings.savedTo.format(result.filePath))

                is DownloadResult.Failure ->
                    toast.showToast(result.throwable.message ?: strings.downloadFailed)
            }
        }
    }

    fun copyLink(wallpaper: Wallpaper) {
        clipboard.setText(AnnotatedString(wallpaper.fullUrl))
        toast.showToast(strings.linkCopied)
    }

    fun openPhotographer(wallpaper: Wallpaper) {
        runCatching { uriHandler.openUri(wallpaper.photographerUrl) }
            .onFailure { toast.showToast(strings.browserFailed) }
    }
}

@Composable
fun rememberDesktopWallpaperActions(): DesktopWallpaperActions {
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current

    val strings = WallpaperActionStrings(
        settingWallpaper = stringResource(Res.string.desktop_setting_wallpaper),
        wallpaperSet = stringResource(Res.string.desktop_wallpaper_set),
        wallpaperFailed = stringResource(Res.string.desktop_wallpaper_failed),
        downloading = stringResource(Res.string.desktop_downloading),
        savedTo = stringResource(Res.string.desktop_saved_to),
        downloadFailed = stringResource(Res.string.desktop_download_failed),
        linkCopied = stringResource(Res.string.desktop_link_copied),
        browserFailed = stringResource(Res.string.desktop_browser_failed)
    )

    return remember(scope, clipboard, uriHandler, strings) {
        DesktopWallpaperActions(scope, clipboard, uriHandler, strings)
    }
}
