package com.google.wallpaperapp.core.platform

import java.util.Locale

actual class LocaleManager actual constructor() {
    actual fun changeLocale(languageCode: String) {
        val locale = Locale.forLanguageTag(languageCode)
        Locale.setDefault(locale)
        appLocaleFlow.value = locale.toLanguageTag()
    }
}
