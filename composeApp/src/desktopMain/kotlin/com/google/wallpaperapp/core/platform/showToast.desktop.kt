package com.google.wallpaperapp.core.platform

actual open class ToastManager actual constructor() {
    actual fun showToast(message: String, toastDurationType: ToastDurationType) {
        desktopToastFlow.tryEmit(DesktopToast(message, toastDurationType))
    }
}
