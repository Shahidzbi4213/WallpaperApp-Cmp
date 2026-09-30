package com.google.wallpaperapp.data.local

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/**
 * v1 -> v2: added alt column to pexel_wallpaper_table for similar wallpapers
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE pexel_wallpaper_table ADD COLUMN alt TEXT NOT NULL DEFAULT ''")
    }
}

/**
 * v2 -> v3: carry the landscape and full-resolution urls that Pexels was already returning
 * but that we used to discard.
 *
 * Written by hand rather than falling back to a destructive migration: the wallpaper cache is
 * disposable, but favourite_wallpaper, user_preference and recent_search are user data and a
 * destructive fallback wipes every table, not just the changed one.
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE pexel_wallpaper_table ADD COLUMN landscape TEXT NOT NULL DEFAULT ''")
        connection.execSQL("ALTER TABLE pexel_wallpaper_table ADD COLUMN original TEXT NOT NULL DEFAULT ''")
        connection.execSQL("ALTER TABLE favourite_wallpaper ADD COLUMN landscape TEXT NOT NULL DEFAULT ''")
    }
}

/**
 * v1 -> v3: direct migration from v1 to v3
 */
val MIGRATION_1_3 = object : Migration(1, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE pexel_wallpaper_table ADD COLUMN alt TEXT NOT NULL DEFAULT ''")
        connection.execSQL("ALTER TABLE pexel_wallpaper_table ADD COLUMN landscape TEXT NOT NULL DEFAULT ''")
        connection.execSQL("ALTER TABLE pexel_wallpaper_table ADD COLUMN original TEXT NOT NULL DEFAULT ''")
        connection.execSQL("ALTER TABLE favourite_wallpaper ADD COLUMN landscape TEXT NOT NULL DEFAULT ''")
    }
}
