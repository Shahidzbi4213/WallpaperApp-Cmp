package com.google.wallpaperapp.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.cachedIn
import com.google.wallpaperapp.data.repositories.RecentSearchRepository
import com.google.wallpaperapp.data.repositories.SearchWallpapersRepository
import com.google.wallpaperapp.domain.models.RecentSearch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repo: RecentSearchRepository,
    private val searchRepo: SearchWallpapersRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchState())

    val recentSearches = repo.recentSearches.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchedWallpapers = _searchState.debounce(200).flatMapLatest { state ->
        searchRepo.getSearchWallpapers(state.searchQuery)
    }.distinctUntilChanged().cachedIn(viewModelScope)


    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> {
                _searchState.update { it.copy(searchQuery = event.query) }
            }

            is SearchEvent.ClearQuery -> {
                _searchState.update { it.copy(searchQuery = "") }
            }

            is SearchEvent.OnExpandChange -> {
                _searchState.update { it.copy(isExpanded = event.value) }
            }

            is SearchEvent.SaveRecentSearch -> {
                viewModelScope.launch {
                    repo.saveRecent(RecentSearch(event.query))
                }
            }

            is SearchEvent.ClearAllRecent -> {
                viewModelScope.launch {
                    repo.clearAllRecent()
                }

            }


        }
    }


}