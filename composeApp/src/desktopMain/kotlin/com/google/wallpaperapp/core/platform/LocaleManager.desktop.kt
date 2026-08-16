package com.google.wallpaperapp.core.platform

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

private val _desktopLocale = MutableStateFlow(Locale.getDefault().toLanguageTag())

/**
 * Compose Resources reads [Locale.getDefault] at composition time and does not observe changes,
 * so DesktopShell keys its content on this flow to force a remount after a language switch.
 *
 * ponytail: full remount rather than a locale CompositionLocal -- Compose Resources 1.11 has no
 * supported override hook. Revisit if one lands.
 */
val desktopLocale: StateFlow<String> = _desktopLocale

actual class LocaleManager actual constructor() {
    actual fun changeLocale(languageCode: String) {
        val locale = Locale.forLanguageTag(languageCode)
        Locale.setDefault(locale)
        _desktopLocale.value = locale.toLanguageTag()
    }
}
