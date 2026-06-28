package com.google.wallpaperapp.ui.screens.main

sealed interface MainNavigationAction {
    data object ToSearch : MainNavigationAction
    data object ToLanguage : MainNavigationAction

    data class ToFavouriteDetail(val id: Long, val url: String) : MainNavigationAction

    data class ToWallpaperDetail(val id: Long, val url: String) : MainNavigationAction
    data class ToCategoryDetail(val category: String) : MainNavigationAction
}
