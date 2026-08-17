package com.google.wallpaperapp.core.platform

actual object AppLogger {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        System.err.println("ERROR: [$tag] $message")
        throwable?.printStackTrace()
    }

    actual fun d(tag: String, message: String) {
        println("DEBUG: [$tag] $message")
    }

    actual fun i(tag: String, message: String) {
        println("INFO: [$tag] $message")
    }
}
