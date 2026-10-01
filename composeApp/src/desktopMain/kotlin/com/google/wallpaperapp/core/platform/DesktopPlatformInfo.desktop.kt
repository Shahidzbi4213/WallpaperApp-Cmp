package com.google.wallpaperapp.core.platform

actual fun getDesktopStoragePaths(): DesktopStoragePaths? = DesktopStoragePaths(
    downloadsFolder = downloadsDir().absolutePath,
    appDataFolder = appDataDir().absolutePath
)

actual fun getPlatformDisplayName(): String =
    currentOs.name.lowercase().replaceFirstChar { it.uppercase() }
