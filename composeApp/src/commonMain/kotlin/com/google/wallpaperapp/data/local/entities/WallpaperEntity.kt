package com.google.wallpaperapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("pexel_wallpaper_table")
data class WallpaperEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @ColumnInfo("photographer")
    val photographerName: String,
    @ColumnInfo("photographer_url")
    val photographerUrl: String,
    @Embedded val wallpaperSource: SrcEntity,
    var page: Int = 0
)