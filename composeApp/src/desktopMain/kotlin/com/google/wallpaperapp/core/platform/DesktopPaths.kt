package com.google.wallpaperapp.core.platform

import java.io.File

enum class DesktopOs { MAC, WINDOWS, LINUX, UNKNOWN }

/**
 * The single place the app branches on operating system. Everything else -- the database
 * location, the downloads folder, the "set as wallpaper" command -- asks this.
 */
val currentOs: DesktopOs by lazy {
    val name = System.getProperty("os.name").orEmpty().lowercase()
    when {
        name.contains("mac") || name.contains("darwin") -> DesktopOs.MAC
        name.contains("win") -> DesktopOs.WINDOWS
        name.contains("nux") || name.contains("nix") || name.contains("aix") -> DesktopOs.LINUX
        else -> DesktopOs.UNKNOWN
    }
}

private const val APP_DIR_NAME = "Screeny"

private val userHome: File get() = File(System.getProperty("user.home"))

/**
 * Per-OS application data directory, following each platform's own convention.
 * Created on first access so callers never have to remember to mkdirs().
 */
fun appDataDir(): File {
    val dir = when (currentOs) {
        DesktopOs.MAC -> File(userHome, "Library/Application Support/$APP_DIR_NAME")
        DesktopOs.WINDOWS -> {
            val appData = System.getenv("APPDATA")
            if (appData.isNullOrBlank()) File(userHome, ".screeny") else File(appData, APP_DIR_NAME)
        }

        DesktopOs.LINUX, DesktopOs.UNKNOWN -> {
            val xdg = System.getenv("XDG_DATA_HOME")
            if (xdg.isNullOrBlank()) File(userHome, ".local/share/screeny") else File(xdg, "screeny")
        }
    }
    return dir.ensureExists()
}

/** Where downloaded wallpapers land. Visible to the user, unlike [appDataDir]. */
fun downloadsDir(): File = File(userHome, "Pictures/$APP_DIR_NAME").ensureExists()

private fun File.ensureExists(): File = apply {
    if (!exists()) mkdirs()
}
