package com.google.wallpaperapp.di

import com.google.wallpaperapp.data.local.dao.CommonDao
import com.google.wallpaperapp.data.local.dao.FavouriteWallpaperDao
import com.google.wallpaperapp.ui.screens.splash.SplashViewModel
import com.google.wallpaperapp.data.local.dao.WallpaperDao
import com.google.wallpaperapp.data.local.dao.WallpaperRemoteKeysDao
import com.google.wallpaperapp.data.local.dao.RecentSearchDao
import com.google.wallpaperapp.data.local.dao.UserPreferenceDao
import com.google.wallpaperapp.data.remote.provider.WallpaperAggregator
import com.google.wallpaperapp.data.repositories.FavouriteRepo
import com.google.wallpaperapp.data.repositories.RecentSearchRepository
import com.google.wallpaperapp.data.repositories.SearchWallpapersRepository
import com.google.wallpaperapp.data.repositories.UserPreferenceRepo
import com.google.wallpaperapp.data.repositories.WallpaperRepository
import com.google.wallpaperapp.ui.screens.category.CategoryViewModel
import com.google.wallpaperapp.ui.screens.detail.SimilarWallpapersViewModel
import com.google.wallpaperapp.ui.screens.favourite.FavouriteViewModel
import com.google.wallpaperapp.ui.screens.home.HomeScreenViewModel
import com.google.wallpaperapp.ui.screens.languages.LanguageViewModel
import com.google.wallpaperapp.ui.screens.search.SearchViewModel
import com.google.wallpaperapp.ui.screens.settings.SettingViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.KoinViewModel
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

    @KoinViewModel
    fun provideSimilarWallpapersViewModel(repository: SearchWallpapersRepository): SimilarWallpapersViewModel {
        return SimilarWallpapersViewModel(repository)
    }


    @Factory
    fun provideSearchRepo(aggregator: WallpaperAggregator): SearchWallpapersRepository {
        return SearchWallpapersRepository(aggregator)
    }

    @Single
    fun provideWallpaperRepo(
        wallpaperDao: WallpaperDao,
        keysDao: WallpaperRemoteKeysDao,
        commonDao: CommonDao,
        aggregator: WallpaperAggregator
    ): WallpaperRepository {
        return WallpaperRepository(wallpaperDao, keysDao, commonDao, aggregator)
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

    @Factory
    fun provideRecentSearchRepository(recentSearchDao: RecentSearchDao): RecentSearchRepository {
        return RecentSearchRepository(recentSearchDao)
    }


    @KoinViewModel
    fun provideSearchViewModel(searchWallpapersRepository: SearchWallpapersRepository,
                               recentSearchRepository: RecentSearchRepository): SearchViewModel {
        return SearchViewModel(searchRepo = searchWallpapersRepository, repo = recentSearchRepository)
    }
}
