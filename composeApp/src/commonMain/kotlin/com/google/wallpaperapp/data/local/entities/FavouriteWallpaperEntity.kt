package com.google.wallpaperapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Entity("favourite_wallpaper")
data class FavouriteWallpaperEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    /** Preview URL, kept under its original column name so existing rows survive. */
    val wallpaper: String,
    val source: String = "",
    @ColumnInfo("full_url") val fullUrl: String = "",
    @ColumnInfo("author_name") val authorName: String = "",
    @ColumnInfo("author_url") val authorUrl: String = "",
    @ColumnInfo("source_page_url") val sourcePageUrl: String = "",
    val timeStamp: Long = Clock.System.now().epochSeconds
)
