package com.google.wallpaperapp.core.platform

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openUrl(url: String) {
    if (url.isBlank()) return
    val nsUrl = NSURL.URLWithString(url) ?: return
    UIApplication.sharedApplication.openURL(nsUrl)
}
