package com.google.wallpaperapp.core.platform

// commonMain
enum class PlatformType {
    ANDROID,
    IOS
}

expect fun getPlatformType(): PlatformType
