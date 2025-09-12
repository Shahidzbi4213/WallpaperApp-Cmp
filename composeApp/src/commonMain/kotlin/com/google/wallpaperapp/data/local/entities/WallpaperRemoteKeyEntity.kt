package com.google.wallpaperapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Entity("pexel_wallpaper_remote_keys_table")
data class WallpaperRemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val wallpaperId: Long,
    val prevPage: Int?,
    val nextPage: Int?,
    val page: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)