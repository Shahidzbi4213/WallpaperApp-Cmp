package com.google.wallpaperapp.data.local

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

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
