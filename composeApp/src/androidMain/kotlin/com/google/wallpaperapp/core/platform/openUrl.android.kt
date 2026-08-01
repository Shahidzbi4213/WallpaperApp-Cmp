package com.google.wallpaperapp.core.platform

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import org.koin.core.component.KoinComponent

private object UrlOpener : KoinComponent {
    fun context(): Context = getKoin().get()
}

actual fun openUrl(url: String) {
    if (url.isBlank()) return
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    runCatching { UrlOpener.context().startActivity(intent) }
}
