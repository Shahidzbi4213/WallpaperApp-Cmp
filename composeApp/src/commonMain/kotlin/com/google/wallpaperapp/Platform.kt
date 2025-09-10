package com.google.wallpaperapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform