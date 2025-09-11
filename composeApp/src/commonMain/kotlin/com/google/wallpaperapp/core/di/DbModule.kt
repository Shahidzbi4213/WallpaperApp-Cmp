package com.google.wallpaperapp.core.di

import com.google.wallpaperapp.data.local.ScreenyDatabase
import com.google.wallpaperapp.data.local.dao.PexelWallpaperDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperRemoteKeysDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
class DbModule {

    @Single
    fun provideWallpaperDao(db: ScreenyDatabase): PexelWallpaperDao{
        return db.wallpaperDao()
    }

    @Single
    fun provideRemoteKeysDao(db: ScreenyDatabase): PexelWallpaperRemoteKeysDao{
        return db.remoteKeysDao()
    }
}