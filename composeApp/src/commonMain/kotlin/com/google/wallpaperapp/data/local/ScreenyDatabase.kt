package com.google.wallpaperapp.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.google.wallpaperapp.data.local.dao.CommonDao
import com.google.wallpaperapp.data.local.dao.FavouriteWallpaperDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperRemoteKeysDao
import com.google.wallpaperapp.data.local.entities.FavouriteWallpaperEntity
import com.google.wallpaperapp.data.local.entities.WallpaperEntity
import com.google.wallpaperapp.data.local.entities.WallpaperRemoteKeyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [FavouriteWallpaperEntity::class, WallpaperEntity::class,
        WallpaperRemoteKeyEntity::class], version = 1, exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class ScreenyDatabase : RoomDatabase() {

    abstract fun favouriteWallpaperDao(): FavouriteWallpaperDao

    abstract fun wallpaperDao(): PexelWallpaperDao

    abstract fun remoteKeysDao(): PexelWallpaperRemoteKeysDao

    abstract fun commonDao(): CommonDao

    companion object Companion {
        const val SCREENY_DATABASE = "screeny.db"
    }
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<ScreenyDatabase> {
    override fun initialize(): ScreenyDatabase
}


fun getRoomDatabase(
    builder: RoomDatabase.Builder<ScreenyDatabase>
): ScreenyDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(true)
        .build()
}