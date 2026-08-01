package com.google.wallpaperapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CommonDao {

    @Transaction
    suspend fun clearAllWallpapers() {
        deleteAllWallpapers()
        // Previously this called deleteAllWallpapers() a second time, so remote
        // keys were never cleared and leaked on every refresh.
        deleteAllRemoteKeys()
    }

    @Query("DELETE FROM wallpaper_table")
    suspend fun deleteAllWallpapers()

    @Query("DELETE FROM wallpaper_remote_keys_table")
    suspend fun deleteAllRemoteKeys()
}
