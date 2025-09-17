package com.google.wallpaperapp.di

import com.google.wallpaperapp.data.local.dao.CommonDao
import com.google.wallpaperapp.data.local.dao.FavouriteWallpaperDao
import com.google.wallpaperapp.ui.screens.splash.SplashViewModel
import com.google.wallpaperapp.data.local.dao.PexelWallpaperDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperRemoteKeysDao
import com.google.wallpaperapp.data.local.dao.UserPreferenceDao
import com.google.wallpaperapp.data.remote.PexelWallpapersApi
import com.google.wallpaperapp.data.repositories.FavouriteRepo
import com.google.wallpaperapp.data.repositories.SearchWallpapersRepository
import com.google.wallpaperapp.data.repositories.UserPreferenceRepo
import com.google.wallpaperapp.data.repositories.WallpaperRepository
import com.google.wallpaperapp.ui.screens.category.CategoryViewModel
import com.google.wallpaperapp.ui.screens.favourite.FavouriteViewModel
import com.google.wallpaperapp.ui.screens.home.HomeScreenViewModel
import com.google.wallpaperapp.ui.screens.languages.LanguageViewModel
import com.google.wallpaperapp.ui.screens.settings.SettingViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
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


    @KoinViewModel
    fun provideCategoryViewModel(repository: SearchWallpapersRepository): CategoryViewModel {
        return CategoryViewModel(repository)
    }


    @Factory
    fun provideSearchRepo(api: PexelWallpapersApi): SearchWallpapersRepository {
        return SearchWallpapersRepository(api)
    }

    @Single
    fun provideWallpaperRepo(
        wallpaperDao: PexelWallpaperDao,
        keysDao: PexelWallpaperRemoteKeysDao,
        commonDao: CommonDao,
        api: PexelWallpapersApi
    ): WallpaperRepository {
        return WallpaperRepository(wallpaperDao, keysDao, commonDao, api)
    }

    @Single
    fun provideUserPrefRepo(dao: UserPreferenceDao): UserPreferenceRepo = UserPreferenceRepo(dao)


    @KoinViewModel
    fun provideSettingViewModel(userPreferenceRepo: UserPreferenceRepo): SettingViewModel {
        return SettingViewModel(userPreferenceRepo)
    }


    @KoinViewModel
    fun provideLanguageViewModel(userPreferenceRepo: UserPreferenceRepo): LanguageViewModel {
        return LanguageViewModel(userPreferenceRepo)
    }
}