package com.google.wallpaperapp.domain.mappers

import com.google.wallpaperapp.data.local.entities.FavouriteWallpaperEntity
import com.google.wallpaperapp.data.local.entities.SrcEntity
import com.google.wallpaperapp.data.local.entities.WallpaperEntity
import com.google.wallpaperapp.data.remote.models.SrcResponse
import com.google.wallpaperapp.data.remote.models.WallpaperResponse
import com.google.wallpaperapp.domain.models.FavouriteWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper


fun WallpaperResponse.toWallpaperEntity(): WallpaperEntity {
    return WallpaperEntity(
        id = id,
        photographerName = photographerName,
        photographerUrl = photographerUrl,
        wallpaperSource = wallpaperSource.toSrcEntity()
    )
}

fun WallpaperResponse.toWallpaper(): Wallpaper {
    return Wallpaper(
        id = id,
        photographerName = photographerName,
        photographerUrl = photographerUrl,
        portrait = wallpaperSource.portrait,
        medium = wallpaperSource.medium,
        small = wallpaperSource.small
    )
}

fun SrcResponse.toSrcEntity() = SrcEntity(
    medium = medium,
    portrait = portrait,
    small = small
)

fun WallpaperEntity.toWallpaper(): Wallpaper {
    return Wallpaper(
        id = id,
        medium = wallpaperSource.medium,
        small = wallpaperSource.small,
        portrait = wallpaperSource.portrait,
        photographerUrl = photographerUrl,
        photographerName = photographerName
    )
}

fun FavouriteWallpaperEntity.toFavouriteWallpaper(): FavouriteWallpaper {
    return FavouriteWallpaper(id = id, wallpaper = wallpaper)
}

fun List<FavouriteWallpaperEntity>.toFavouriteWallpapers(): List<FavouriteWallpaper> = map { it.toFavouriteWallpaper() }


fun Wallpaper.toFavouriteWallpaper(): FavouriteWallpaper = FavouriteWallpaper(wallpaper = portrait, id = id)