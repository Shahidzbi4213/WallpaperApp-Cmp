package com.google.wallpaperapp.ui.screens.languages

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wallpaperapp.data.repositories.UserPreferenceRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LanguageViewModel(private val userPreferenceRepo: UserPreferenceRepo) : ViewModel() {

    private val defaultLanguage by lazy{
        LANGUAGES_LIST.find { it.languageCode.contains(Locale.current.language) }
            ?: LANGUAGES_LIST.first { it.languageCode.contains("en") }
    }


    val languagesList = userPreferenceRepo.uerPreference
        .map { preference ->
            LANGUAGES_LIST.filter { it.languageCode != preference.languageCode }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LANGUAGES_LIST)


    val currentLanguage = userPreferenceRepo.uerPreference.map { preference ->
        LANGUAGES_LIST.first { it.languageCode == preference.languageCode }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), defaultLanguage)

    var localSelected = MutableStateFlow<Language?>(null)
        private set


    fun updateCurrentLanguage(language: Language) {
        viewModelScope.launch {
            userPreferenceRepo.updateAppLanguage(language.languageCode)
        }
    }

    fun updateLocalLanguageSelection(language: Language?) {
        viewModelScope.launch {
            localSelected.value = language
        }
    }
}