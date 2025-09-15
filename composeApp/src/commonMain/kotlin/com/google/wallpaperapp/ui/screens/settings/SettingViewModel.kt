package com.google.wallpaperapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wallpaperapp.data.repositories.UserPreferenceRepo
import com.google.wallpaperapp.domain.models.UserPreferences
import com.google.wallpaperapp.utils.AppMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch




class SettingViewModel(private val preferenceRepo: UserPreferenceRepo) : ViewModel() {

    val userPreference = preferenceRepo.uerPreference.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserPreferences(
            languageCode = "en",
            appMode = 0,
            shouldShowDynamicColor = true
        )
    )

    private val _state = MutableStateFlow(SettingScreenState())
    var state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingScreenState())


    fun onEvent(event: SettingEvent) {
        when (event) {
            SettingEvent.ToggleAppModeDialog -> {
                _state.update { it.copy(showAppModeDialog = !it.showAppModeDialog) }
            }

            SettingEvent.ToggleDynamicDialog -> {
                _state.update { it.copy(showDynamicDialog = !it.showDynamicDialog) }
            }

            SettingEvent.ToggleRateUsDialog -> {
                _state.update { it.copy(showRateUsDialog = !it.showRateUsDialog) }
            }

            is SettingEvent.UpdateDynamicColor -> {
                viewModelScope.launch {
                    preferenceRepo.updateDynamicColor(event.isDynamicColor)
                }
            }

            is SettingEvent.UpdateAppMode -> {
                viewModelScope.launch {
                    preferenceRepo.updateAppMode(event.appMode.ordinal)
                }
            }


        }
    }


}