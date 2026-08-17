package com.google.wallpaperapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Entity("favourite_wallpaper")
data class FavouriteWallpaperEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    // Always the portrait url. It doubles as the favourite's identity in the existing
    // detail-screen comparison and in deleteViaUrl, so it must not change per platform.
    val wallpaper: String,
    // Added in schema v3 so desktop can render a favourite without a portrait crop.
    // Empty for rows saved before the upgrade, or from a phone.
    val landscape: String = "",
    val timeStamp: Long = Clock.System.now().epochSeconds
)