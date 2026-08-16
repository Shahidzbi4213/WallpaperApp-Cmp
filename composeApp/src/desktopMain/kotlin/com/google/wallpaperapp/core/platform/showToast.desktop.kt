package com.google.wallpaperapp.core.platform

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

data class DesktopToast(val message: String, val duration: ToastDurationType)

private val _desktopToasts = MutableSharedFlow<DesktopToast>(extraBufferCapacity = 8)

/** Drained by DesktopShell into a SnackbarHost -- desktop has no system toast. */
val desktopToasts: SharedFlow<DesktopToast> = _desktopToasts

actual open class ToastManager actual constructor() {
    actual fun showToast(message: String, toastDurationType: ToastDurationType) {
        _desktopToasts.tryEmit(DesktopToast(message, toastDurationType))
    }
}
