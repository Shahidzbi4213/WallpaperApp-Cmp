package com.google.wallpaperapp.core.platform

data class DesktopStoragePaths(
    val downloadsFolder: String,
    val appDataFolder: String
)

expect fun getDesktopStoragePaths(): DesktopStoragePaths?

expect fun getPlatformDisplayName(): String
