package com.google.wallpaperapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.wallpaperapp.ui.composables.collectAsLazyPagingItems
import com.google.wallpaperapp.ui.screens.home.HomeScreenViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {

            val homeScreenViewModel = koinViewModel<HomeScreenViewModel>()
            val wallpapers = homeScreenViewModel.wallpapers.collectAsLazyPagingItems()

            App(wallpapers)
        }
    }
}