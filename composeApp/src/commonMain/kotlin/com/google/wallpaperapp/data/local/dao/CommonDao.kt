package com.google.wallpaperapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CommonDao {

    @Transaction
    suspend fun clearAllWallpapers() {
        deleteAllWallpapers()
        deleteAllWallpapers()
    }

    @Query("DELETE FROM pexel_wallpaper_table")
    suspend fun deleteAllWallpapers()

    @Query("DELETE FROM pexel_wallpaper_remote_keys_table")
    suspend fun deleteAllRemoteKeys()
}