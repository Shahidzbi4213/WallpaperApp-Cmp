package com.google.wallpaperapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.wallpaperapp.data.local.entities.FavouriteWallpaperEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteWallpaperDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavourite(favouriteWallpaper: FavouriteWallpaperEntity)

    @Delete
    suspend fun removeFromFavourite(wallpaper: FavouriteWallpaperEntity)

    @Query("SELECT * FROM favourite_wallpaper WHERE id=:id limit 1")
    suspend fun getFavouriteById(id: Long): FavouriteWallpaperEntity?


    @Query("DELETE FROM favourite_wallpaper WHERE wallpaper=:url")
    suspend fun deleteViaUrl(url: String)

    @Query("SELECT * FROM favourite_wallpaper ORDER BY timeStamp Desc")
    fun getAllFavourites(): Flow<List<FavouriteWallpaperEntity>>

}