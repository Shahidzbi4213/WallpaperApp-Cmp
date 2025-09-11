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
    val wallpaper: String,
    val timeStamp: Long = Clock.System.now().epochSeconds
)