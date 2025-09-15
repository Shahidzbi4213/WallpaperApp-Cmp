package com.google.wallpaperapp.ui.screens.settings

import com.google.wallpaperapp.utils.AppMode

sealed interface SettingEvent {
    data object ToggleDynamicDialog : SettingEvent
    data object ToggleAppModeDialog : SettingEvent
    data class UpdateDynamicColor(val isDynamicColor: Boolean) : SettingEvent
    data class UpdateAppMode(val appMode: AppMode) : SettingEvent
    data object ToggleRateUsDialog : SettingEvent
}

