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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<WallpaperRemoteKeyEntity>)




}