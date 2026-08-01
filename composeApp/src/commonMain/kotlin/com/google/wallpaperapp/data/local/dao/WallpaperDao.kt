package com.google.wallpaperapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.wallpaperapp.data.local.entities.WallpaperEntity


@Dao
interface WallpaperDao {

    /**
     * Ordered by page then insertion order, so the blended ordering the
     * aggregator produced survives a replay from cache.
     */
    @Query("SELECT * FROM wallpaper_table ORDER BY page, rowid")
    fun getAllWallpapers(): PagingSource<Int, WallpaperEntity>

    @Query("SELECT COUNT(*) FROM wallpaper_table")
    suspend fun getWallpaperCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWallpapers(wallpapers: List<WallpaperEntity>)
}
