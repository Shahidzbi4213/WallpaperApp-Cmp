package com.google.wallpaperapp.core.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.google.wallpaperapp.data.local.ScreenyDatabase
import com.google.wallpaperapp.data.local.dao.CommonDao
import com.google.wallpaperapp.data.local.dao.FavouriteWallpaperDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperRemoteKeysDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
class DbModule {

    @Single
    fun provideScreenyDb(builder: RoomDatabase.Builder<ScreenyDatabase>): ScreenyDatabase {
        return builder.fallbackToDestructiveMigrationOnDowngrade(true).setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
    }

    @Single
    fun provideFavouriteWallpapersDao(db: ScreenyDatabase): FavouriteWallpaperDao {
        return db.favouriteWallpaperDao()
    }

    @Single
    fun provideWallpaperDao(db: ScreenyDatabase): PexelWallpaperDao {
        return db.wallpaperDao()
    }

    @Single
    fun provideRemoteKeysDao(db: ScreenyDatabase): PexelWallpaperRemoteKeysDao {
        return db.remoteKeysDao()
    }

    @Single
    fun provideCommonDao(db: ScreenyDatabase): CommonDao {
        return db.commonDao()
    }
}