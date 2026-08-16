package com.google.wallpaperapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wallpaperapp.data.repositories.UserPreferenceRepo
import com.google.wallpaperapp.domain.models.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update




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
            SettingEvent.ToggleRateUsDialog -> {
                _state.update { it.copy(showRateUsDialog = !it.showRateUsDialog) }
            }
        }
    }


}