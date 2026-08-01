package com.google.wallpaperapp.ui.screens.main

sealed interface MainNavigationAction {
    data object ToSearch : MainNavigationAction
    data object ToLanguage : MainNavigationAction

    data class ToFavouriteDetail(val id: String, val url: String) : MainNavigationAction

    data class ToWallpaperDetail(val id: String, val url: String) : MainNavigationAction
    data class ToCategoryDetail(val name: String, val query: String) : MainNavigationAction
    data class ToMeshGradientDetail(val index: Int) : MainNavigationAction
}
