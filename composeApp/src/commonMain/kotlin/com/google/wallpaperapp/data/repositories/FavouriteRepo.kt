package com.google.wallpaperapp.data.repositories

import com.google.wallpaperapp.data.local.dao.FavouriteWallpaperDao
import com.google.wallpaperapp.data.local.entities.FavouriteWallpaperEntity
import com.google.wallpaperapp.domain.mappers.toFavouriteWallpapers
import com.google.wallpaperapp.domain.models.FavouriteWallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavouriteRepo(private val dao: FavouriteWallpaperDao) {

    private val ioDispatcher = Dispatchers.IO

    val getAllFavourites: Flow<List<FavouriteWallpaper>>
        get() = dao.getAllFavourites()
            .map { wallpapers -> wallpapers.toFavouriteWallpapers() }

    suspend fun addOrRemove(wallpaper: FavouriteWallpaper) {
        withContext(ioDispatcher) {
            val savedWallpaper = dao.getFavouriteById(wallpaper.id)

            if (savedWallpaper != null) {
                dao.removeFromFavourite(savedWallpaper)
            } else {
                val favouriteWallpaper = FavouriteWallpaperEntity(
                    wallpaper.id, wallpaper.wallpaper,
                )
                dao.addToFavourite(favouriteWallpaper)
            }
        }
    }


    suspend fun removeWallpaper(url: String) {
        withContext(ioDispatcher) {
            dao.deleteViaUrl(url)
        }
    }


}