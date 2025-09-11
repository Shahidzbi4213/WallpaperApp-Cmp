package com.google.wallpaperapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.wallpaperapp.data.local.entities.WallpaperRemoteKeyEntity

@Dao
interface PexelWallpaperRemoteKeysDao {

    @Query("SELECT * FROM pexel_wallpaper_remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeyByWallpaperId(id: Long): WallpaperRemoteKeyEntity?

    @Query("Select created_at From pexel_wallpaper_remote_keys_table Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<WallpaperRemoteKeyEntity>)

    @Query("DELETE FROM pexel_wallpaper_remote_keys_table")
    suspend fun deleteAllRemoteKeys()


}