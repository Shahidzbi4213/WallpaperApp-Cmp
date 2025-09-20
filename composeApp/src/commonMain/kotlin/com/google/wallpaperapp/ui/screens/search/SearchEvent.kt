package com.google.wallpaperapp.ui.screens.search

sealed interface SearchEvent {

    data class OnQueryChange(val query: String) : SearchEvent
    data object ClearQuery : SearchEvent
    data class OnExpandChange(val value: Boolean) : SearchEvent
    data class SaveRecentSearch(val query: String) : SearchEvent
    data object ClearAllRecent : SearchEvent
}