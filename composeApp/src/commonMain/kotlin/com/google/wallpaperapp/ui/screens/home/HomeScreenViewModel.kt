package com.google.wallpaperapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.wallpaperapp.data.repositories.WallpaperRepository

class HomeScreenViewModel(repo: WallpaperRepository) : ViewModel() {

    val wallpapers = repo.getAllWallpapers()
        .cachedIn(viewModelScope)
}