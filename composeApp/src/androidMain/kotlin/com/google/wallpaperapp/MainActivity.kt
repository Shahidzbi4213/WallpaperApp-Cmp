package com.google.wallpaperapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.wallpaperapp.core.platform.setActivityProvider
import com.google.wallpaperapp.ui.App
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setActivityProvider { this }
        setContent {
            App()
        }
    }
}
