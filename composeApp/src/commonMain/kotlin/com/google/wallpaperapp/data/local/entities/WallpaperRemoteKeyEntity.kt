package com.google.wallpaperapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.screeny.data.utils.Constant.PEXEL_WALLPAPER_REMOTE_KEYS_TABLE
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Entity(PEXEL_WALLPAPER_REMOTE_KEYS_TABLE)
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