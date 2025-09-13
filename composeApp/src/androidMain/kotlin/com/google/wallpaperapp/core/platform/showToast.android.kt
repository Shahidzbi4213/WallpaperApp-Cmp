package com.google.wallpaperapp.core.platform

import android.app.Activity
import android.widget.Toast

actual open class ToastManager actual constructor() {
    actual fun showToast(message: String, toastDurationType: ToastDurationType) {
        val context = activityProvider.invoke()
        val duration = when (toastDurationType) {
            ToastDurationType.SHORT -> Toast.LENGTH_SHORT
            ToastDurationType.LONG -> Toast.LENGTH_LONG
        }
        Toast.makeText(context, message, duration).show()
    }
}

private var activityProvider: () -> Activity? = {
    null
}

fun setActivityProvider(provider: () -> Activity?) {
    activityProvider = provider
}