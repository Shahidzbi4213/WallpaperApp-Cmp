package com.google.wallpaperapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user_preference")
data class UserPreferenceEntity(

    @PrimaryKey(autoGenerate = false)
    var id: Int = 1,
    var languageCode: String = "en",

    var appMode: Int = 0,
    var shouldShowDynamicColor: Boolean = true
)