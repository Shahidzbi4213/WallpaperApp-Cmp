package com.google.wallpaperapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.wallpaperapp.data.local.entities.WallpaperEntity


@Dao
interface PexelWallpaperDao {

    @Query("SELECT * FROM pexel_wallpaper_table order by page")
    fun getAllWallpapers(): PagingSource<Int, WallpaperEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWallpapers(wallpapers: List<WallpaperEntity>)


}