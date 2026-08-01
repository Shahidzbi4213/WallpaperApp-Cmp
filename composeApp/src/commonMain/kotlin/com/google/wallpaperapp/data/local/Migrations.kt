package com.google.wallpaperapp.data.local

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/**
 * v2 -> v3: multi-source support.
 *
 * Wallpaper ids become `"<source>:<providerId>"` strings, because provider ids
 * are only unique within one provider's catalogue.
 *
 * The two feed tables are a disposable cache, so they are dropped and recreated.
 * `favourite_wallpaper` holds user data and is migrated in place — every
 * pre-existing row came from Pexels, so its ids gain a `pexels:` prefix.
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS pexel_wallpaper_table")
        connection.execSQL("DROP TABLE IF EXISTS pexel_wallpaper_remote_keys_table")

        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS wallpaper_table (
                id TEXT NOT NULL PRIMARY KEY,
                source TEXT NOT NULL,
                thumb_url TEXT NOT NULL,
                preview_url TEXT NOT NULL,
                full_url TEXT NOT NULL,
                width INTEGER NOT NULL,
                height INTEGER NOT NULL,
                alt TEXT NOT NULL,
                author_name TEXT NOT NULL,
                author_url TEXT NOT NULL,
                source_page_url TEXT NOT NULL,
                attribution TEXT,
                download_location TEXT,
                page INTEGER NOT NULL
            )
            """.trimIndent()
        )

        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS wallpaper_remote_keys_table (
                id TEXT NOT NULL PRIMARY KEY,
                prevPage INTEGER,
                nextPage INTEGER,
                page INTEGER NOT NULL
            )
            """.trimIndent()
        )

        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS favourite_wallpaper_new (
                id TEXT NOT NULL PRIMARY KEY,
                wallpaper TEXT NOT NULL,
                source TEXT NOT NULL,
                full_url TEXT NOT NULL,
                author_name TEXT NOT NULL,
                author_url TEXT NOT NULL,
                source_page_url TEXT NOT NULL,
                timeStamp INTEGER NOT NULL
            )
            """.trimIndent()
        )
        connection.execSQL(
            """
            INSERT OR IGNORE INTO favourite_wallpaper_new
                (id, wallpaper, source, full_url, author_name, author_url, source_page_url, timeStamp)
            SELECT 'pexels:' || CAST(id AS TEXT), wallpaper, 'pexels', wallpaper, '', '', '', timeStamp
            FROM favourite_wallpaper
            """.trimIndent()
        )
        connection.execSQL("DROP TABLE favourite_wallpaper")
        connection.execSQL("ALTER TABLE favourite_wallpaper_new RENAME TO favourite_wallpaper")
    }
}
