package com.google.wallpaperapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.google.wallpaperapp.data.repositories.WallpaperRepository
import com.google.wallpaperapp.domain.mappers.toWallpaper
import kotlinx.coroutines.flow.map

class HomeScreenViewModel(private val repo: WallpaperRepository) : ViewModel() {

    val wallpapers = repo.getAllWallpapers()
        .flow
        .map { pagingData ->
            pagingData.map { entity -> entity.toWallpaper() }
        }
        .cachedIn(viewModelScope)
}