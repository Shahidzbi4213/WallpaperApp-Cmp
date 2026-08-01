package com.google.wallpaperapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Cached page of the blended home feed.
 *
 * The primary key is the namespaced [com.google.wallpaperapp.domain.models.Wallpaper.id]
 * (`"pexels:12345"`), because provider ids are only unique within their own
 * catalogue and two providers can easily share a numeric id.
 */
@Entity("wallpaper_table")
data class WallpaperEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val source: String,
    @ColumnInfo("thumb_url") val thumbUrl: String,
    @ColumnInfo("preview_url") val previewUrl: String,
    @ColumnInfo("full_url") val fullUrl: String,
    val width: Int = 0,
    val height: Int = 0,
    @ColumnInfo("alt") val alt: String = "",
    @ColumnInfo("author_name") val authorName: String = "",
    @ColumnInfo("author_url") val authorUrl: String = "",
    @ColumnInfo("source_page_url") val sourcePageUrl: String = "",
    @ColumnInfo("attribution") val attribution: String? = null,
    @ColumnInfo("download_location") val downloadLocation: String? = null,
    var page: Int = 0,
)
