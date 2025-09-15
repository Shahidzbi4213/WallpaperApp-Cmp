package com.google.wallpaperapp.utils

enum class AppMode {

    DEFAULT, LIGHT, DARK;

    companion object{
        fun getModeById(id:Int): AppMode = AppMode.entries[id]
    }
}