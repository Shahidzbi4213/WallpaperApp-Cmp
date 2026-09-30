package com.google.wallpaperapp.core.platform

// commonMain
enum class PlatformType {
    ANDROID,
    IOS,
    DESKTOP,
    WEB
}

expect fun getPlatformType(): PlatformType
