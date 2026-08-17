package com.google.wallpaperapp.core.platform

// commonMain
enum class PlatformType {
    ANDROID,
    IOS,
    DESKTOP
}

expect fun getPlatformType(): PlatformType
