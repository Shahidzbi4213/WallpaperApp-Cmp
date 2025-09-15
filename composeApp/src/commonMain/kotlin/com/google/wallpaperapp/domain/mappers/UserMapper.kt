package com.google.wallpaperapp.domain.mappers

import com.google.wallpaperapp.data.local.entities.UserPreferenceEntity
import com.google.wallpaperapp.domain.models.UserPreferences


fun UserPreferenceEntity.toUserPreference(): UserPreferences {
    return UserPreferences(
        languageCode = languageCode,
        appMode = appMode,
        shouldShowDynamicColor = shouldShowDynamicColor
    )
}