package com.google.wallpaperapp.di

import com.google.wallpaperapp.data.local.dao.FavouriteWallpaperDao
import com.google.wallpaperapp.data.repositories.FavouriteRepo
import com.google.wallpaperapp.ui.screens.favourite.FavouriteViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class FavouriteModule {

    @KoinViewModel
    fun provideFavViewModel(repo: FavouriteRepo): FavouriteViewModel{
        return FavouriteViewModel(repo)
    }


    @Factory
    fun provideFavouriteRepo(dao: FavouriteWallpaperDao): FavouriteRepo{
        return FavouriteRepo(dao)
    }
}