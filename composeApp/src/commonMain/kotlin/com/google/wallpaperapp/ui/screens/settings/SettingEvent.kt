package com.google.wallpaperapp.ui.screens.settings

sealed interface SettingEvent {
    data object ToggleRateUsDialog : SettingEvent
}

