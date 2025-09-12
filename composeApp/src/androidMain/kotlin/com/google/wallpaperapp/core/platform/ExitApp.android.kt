package com.google.wallpaperapp.core.platform

import kotlin.system.exitProcess

actual fun exitApp() {

    exitProcess(-1)
}