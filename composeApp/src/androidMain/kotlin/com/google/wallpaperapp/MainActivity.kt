package com.google.wallpaperapp

import android.graphics.Color.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.wallpaperapp.core.platform.setActivityProvider
import com.google.wallpaperapp.ui.App
import com.google.wallpaperapp.ui.screens.settings.SettingViewModel
import com.google.wallpaperapp.utils.AppMode
import com.google.wallpaperapp.utils.isDarkMode
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

    private val isDark = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initial setup with your own dark-mode handling
        updateSystemBars()

        setActivityProvider { this }
        setContent {
            val settingViewModel = koinViewModel<SettingViewModel>()
            val userPreferences by settingViewModel.userPreference.collectAsStateWithLifecycle()

            // Update state based on your own mode
            isDark.value = isDarkMode(AppMode.getModeById(userPreferences.appMode))

            // Re-apply edge-to-edge whenever dark mode changes
            SideEffect {
                updateSystemBars()
            }

            App()
        }
    }

    private fun updateSystemBars() {
        if (isDark.value) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(TRANSPARENT),
                navigationBarStyle = SystemBarStyle.dark(TRANSPARENT)
            )
        } else {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(
                    TRANSPARENT,
                    TRANSPARENT
                ),
                navigationBarStyle = SystemBarStyle.light(
                    TRANSPARENT,
                    TRANSPARENT
                )
            )
        }
    }
}
