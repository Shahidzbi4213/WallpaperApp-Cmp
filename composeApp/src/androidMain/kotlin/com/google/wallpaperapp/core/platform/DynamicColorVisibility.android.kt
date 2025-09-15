package com.google.wallpaperapp.core.platform

import android.os.Build

actual fun dynamicColorVisibility(): Boolean {

    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

}