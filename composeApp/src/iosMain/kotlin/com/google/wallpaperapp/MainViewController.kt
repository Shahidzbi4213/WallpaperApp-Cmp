package com.google.wallpaperapp

import androidx.compose.ui.window.ComposeUIViewController
import com.google.wallpaperapp.ui.composables.collectAsLazyPagingItems
import com.google.wallpaperapp.ui.screens.home.HomeScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

fun MainViewController() = ComposeUIViewController {

    val homeScreenViewModel = koinViewModel<HomeScreenViewModel>()
    val wallpapers = homeScreenViewModel.wallpapers.collectAsLazyPagingItems()

    App(wallpapers)
}