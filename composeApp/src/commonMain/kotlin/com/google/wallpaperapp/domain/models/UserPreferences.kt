package com.google.wallpaperapp.domain.models

data class UserPreferences(
    var languageCode: String,
    var appMode: Int,
    var shouldShowDynamicColor: Boolean
)