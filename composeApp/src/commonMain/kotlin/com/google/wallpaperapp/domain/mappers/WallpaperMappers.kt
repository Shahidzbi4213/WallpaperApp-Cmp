package com.google.wallpaperapp.domain.mappers

import com.google.wallpaperapp.data.local.entities.FavouriteWallpaperEntity
import com.google.wallpaperapp.data.local.entities.WallpaperEntity
import com.google.wallpaperapp.domain.models.FavouriteWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.WallpaperSource


fun Wallpaper.toWallpaperEntity(page: Int = 0): WallpaperEntity = WallpaperEntity(
    id = id,
    source = source.key,
    thumbUrl = thumbUrl,
    previewUrl = previewUrl,
    fullUrl = fullUrl,
    width = width,
    height = height,
    alt = alt,
    authorName = authorName,
    authorUrl = authorUrl,
    sourcePageUrl = sourcePageUrl,
    attribution = attribution,
    downloadLocation = downloadLocation,
    page = page,
)

fun WallpaperEntity.toWallpaper(): Wallpaper = Wallpaper(
    id = id,
    source = source.toWallpaperSource(),
    thumbUrl = thumbUrl,
    previewUrl = previewUrl,
    fullUrl = fullUrl,
    width = width,
    height = height,
    alt = alt,
    authorName = authorName,
    authorUrl = authorUrl,
    sourcePageUrl = sourcePageUrl,
    attribution = attribution,
    downloadLocation = downloadLocation,
)

fun FavouriteWallpaperEntity.toFavouriteWallpaper(): FavouriteWallpaper = FavouriteWallpaper(
    id = id,
    wallpaper = wallpaper,
    source = source.takeIf { it.isNotBlank() }?.toWallpaperSource(),
    fullUrl = fullUrl.ifBlank { wallpaper },
    authorName = authorName,
    authorUrl = authorUrl,
    sourcePageUrl = sourcePageUrl,
)

fun List<FavouriteWallpaperEntity>.toFavouriteWallpapers(): List<FavouriteWallpaper> =
    map { it.toFavouriteWallpaper() }

fun FavouriteWallpaper.toEntity(): FavouriteWallpaperEntity = FavouriteWallpaperEntity(
    id = id,
    wallpaper = wallpaper,
    source = source?.key.orEmpty(),
    fullUrl = fullUrl,
    authorName = authorName,
    authorUrl = authorUrl,
    sourcePageUrl = sourcePageUrl,
)

fun Wallpaper.toFavouriteWallpaper(): FavouriteWallpaper = FavouriteWallpaper(
    id = id,
    wallpaper = previewUrl,
    source = source,
    fullUrl = fullUrl,
    authorName = authorName,
    authorUrl = authorUrl,
    sourcePageUrl = sourcePageUrl,
)

/** Falls back to Pexels for rows written before the multi-source migration. */
private fun String.toWallpaperSource(): WallpaperSource =
    WallpaperSource.entries.firstOrNull { it.key == this } ?: WallpaperSource.PEXELS
