package com.google.wallpaperapp.core.platform

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

expect class LocaleManager() {
    fun changeLocale(languageCode: String)
}

val appLocaleFlow = MutableStateFlow("en")
val desktopLocale: StateFlow<String> = appLocaleFlow