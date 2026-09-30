package com.google.wallpaperapp.di

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import com.google.wallpaperapp.data.local.MIGRATION_1_2
import com.google.wallpaperapp.data.local.MIGRATION_1_3
import com.google.wallpaperapp.data.local.MIGRATION_2_3
import com.google.wallpaperapp.data.local.ScreenyDatabase
import com.google.wallpaperapp.data.local.dao.CommonDao
import com.google.wallpaperapp.data.local.dao.FavouriteWallpaperDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperRemoteKeysDao
import com.google.wallpaperapp.data.local.dao.RecentSearchDao
import com.google.wallpaperapp.data.local.dao.UserPreferenceDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val dbModule = module {
    single<ScreenyDatabase> {
        val builder = get<RoomDatabase.Builder<ScreenyDatabase>>()
        builder
            .fallbackToDestructiveMigration(true)
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_1_3)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(connection: SQLiteConnection) {
                    super.onCreate(connection)
                    connection.execSQL("INSERT INTO user_preference (languageCode,appMode,shouldShowDynamicColor) VALUES ('en',0,1)")
                }
            })
            .build()
    }

    single<FavouriteWallpaperDao> { get<ScreenyDatabase>().favouriteWallpaperDao() }
    single<PexelWallpaperDao> { get<ScreenyDatabase>().wallpaperDao() }
    single<PexelWallpaperRemoteKeysDao> { get<ScreenyDatabase>().remoteKeysDao() }
    single<CommonDao> { get<ScreenyDatabase>().commonDao() }
    single<UserPreferenceDao> { get<ScreenyDatabase>().userPreferenceDao() }
    single<RecentSearchDao> { get<ScreenyDatabase>().recentSearchDao() }
}