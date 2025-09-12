package com.google.wallpaperapp.ui.screens.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wallpaperapp.data.repositories.FavouriteRepo
import com.google.wallpaperapp.domain.mappers.toFavouriteWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavouriteViewModel(private val favouriteRepo: FavouriteRepo) : ViewModel() {

    val getAllFavourites = favouriteRepo.getAllFavourites
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())



    fun addToFavourite(wallpaper: Wallpaper){
        viewModelScope.launch {
            favouriteRepo.addOrRemove(wallpaper.toFavouriteWallpaper())
        }
    }

    fun removeFromFavourite(wallpaperUrl: String) {
        viewModelScope.launch {
            favouriteRepo.removeWallpaper(wallpaperUrl)
        }
    }
}