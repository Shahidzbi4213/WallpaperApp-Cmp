package com.google.wallpaperapp.core.platform

expect class LocaleManager() {

    fun changeLocale(languageCode: String)
}