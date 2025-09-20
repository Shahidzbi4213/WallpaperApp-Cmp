package com.google.wallpaperapp.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.wallpaperapp.core.platform.BackHandler
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.composables.collectAsLazyPagingItems
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchedWallpaperScreen(
    searchViewModel: SearchViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onWallpaperClick: (Wallpaper, List<Wallpaper>) -> Unit,
) {

    val state by searchViewModel.searchState.collectAsStateWithLifecycle()
    val searchedWallpapers = searchViewModel.searchedWallpapers.collectAsLazyPagingItems()
    val localKeyboard = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val gridState = rememberLazyGridState()


    BackHandler(true, onBack = {
        searchViewModel.onEvent(SearchEvent.OnExpandChange(true))
        searchViewModel.onEvent(SearchEvent.ClearQuery)
        onNavigateBack()
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()

    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = if (!state.isExpanded) 20.dp else 0.dp),
            windowInsets = if (state.isExpanded) {
                WindowInsets(0.dp)
            } else {
                WindowInsets(top = 20.dp)
            },
            inputField = {
                with(searchViewModel) {
                    SearchField(
                        searchQuery = state.searchQuery, isExpanded = state.isExpanded,
                        focusRequester = focusRequester,
                        onQueryChange = { onEvent(SearchEvent.OnQueryChange(it)) },
                        clearQuery = { onEvent(SearchEvent.ClearQuery) },
                        onExpandChange = { onEvent(SearchEvent.OnExpandChange(it)) },
                        onNavigateBack = {
                            onEvent(SearchEvent.OnExpandChange(true))
                            onEvent(SearchEvent.ClearQuery)
                            onNavigateBack()
                        },
                        saveSearchQuery = {
                            onEvent(SearchEvent.SaveRecentSearch(it))
                            localKeyboard?.hide()
                            localFocusManager.clearFocus(true)
                        }

                    )
                }
            },
            expanded = state.isExpanded,
            onExpandedChange = { searchViewModel.onEvent(SearchEvent.OnExpandChange(it)) },
            colors = SearchBarDefaults.colors(
                containerColor = if (state.isExpanded) {
                    MaterialTheme.colorScheme.background
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHigh
                }, dividerColor = MaterialTheme.colorScheme.outline
            ),
            tonalElevation = 0.dp,
        ) {

        }

        AnimatedVisibility(visible = !state.isExpanded) {
            Spacer(modifier = Modifier.height(20.dp))
            ShowWallpapers(searchedWallpapers, gridState, onWallpaperClick)
        }
    }
}

