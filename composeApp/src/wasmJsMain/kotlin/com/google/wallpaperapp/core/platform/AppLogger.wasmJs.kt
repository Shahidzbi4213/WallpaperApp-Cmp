package com.google.wallpaperapp.core.platform

actual object AppLogger {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        println("ERROR: [$tag] $message ${throwable?.message.orEmpty()}")
    }

    actual fun d(tag: String, message: String) {
        println("DEBUG: [$tag] $message")
    }

    actual fun i(tag: String, message: String) {
        println("INFO: [$tag] $message")
    }
}
