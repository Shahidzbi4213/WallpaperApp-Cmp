package com.google.wallpaperapp.core.di

import com.google.wallpaperapp.ui.screens.splash.SplashViewModel
import com.google.wallpaperapp.data.local.dao.PexelWallpaperDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperRemoteKeysDao
import com.google.wallpaperapp.data.remote.PexelWallpapersApi
import com.google.wallpaperapp.data.repositories.WallpaperRepository
import com.google.wallpaperapp.ui.screens.home.HomeScreenViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
class AppModule {

    @KoinViewModel
    fun provideSplashViewModel(): SplashViewModel {
        return SplashViewModel()
    }

    @KoinViewModel
    fun provideHomeViewModel(repository: WallpaperRepository): HomeScreenViewModel {
        return HomeScreenViewModel(repository)
    }


    @Single
    fun provideWallpaperRepo(
        wallpaperDao: PexelWallpaperDao,
        keysDao: PexelWallpaperRemoteKeysDao,
        api: PexelWallpapersApi
    ): WallpaperRepository {
        return WallpaperRepository(wallpaperDao, keysDao, api)
    }
}