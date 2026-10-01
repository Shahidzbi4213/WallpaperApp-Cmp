package com.google.wallpaperapp.core.platform

expect open class ToastManager() {
    fun showToast(message: String, toastDurationType: ToastDurationType = ToastDurationType.SHORT)
}


enum class ToastDurationType {
    SHORT,
    LONG
}

data class DesktopToast(val message: String, val duration: ToastDurationType)

val desktopToastFlow: kotlinx.coroutines.flow.MutableSharedFlow<DesktopToast> = kotlinx.coroutines.flow.MutableSharedFlow(extraBufferCapacity = 8)
val desktopToasts: kotlinx.coroutines.flow.SharedFlow<DesktopToast> = desktopToastFlow