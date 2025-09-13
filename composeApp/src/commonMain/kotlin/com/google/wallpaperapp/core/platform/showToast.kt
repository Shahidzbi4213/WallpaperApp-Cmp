package com.google.wallpaperapp.core.platform

expect open class ToastManager() {
    fun showToast(message: String, toastDurationType: ToastDurationType = ToastDurationType.SHORT)
}


enum class ToastDurationType {
    SHORT,
    LONG
}