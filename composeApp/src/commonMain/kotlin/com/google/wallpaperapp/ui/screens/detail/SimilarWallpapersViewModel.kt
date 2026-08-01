package com.google.wallpaperapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.wallpaperapp.data.repositories.SearchWallpapersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

class SimilarWallpapersViewModel(private val repo: SearchWallpapersRepository) : ViewModel() {

    private val _query = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val similarWallpapers = _query.filter { it != null }.flatMapLatest {
        if (it!!.isEmpty()) {
            kotlinx.coroutines.flow.flowOf(androidx.paging.PagingData.empty())
        } else {
            repo.getSearchWallpapers(it)
        }
    }.cachedIn(viewModelScope)

    fun fetchSimilar(altText: String) {
        val query = extractQuery(altText)
        _query.update { query }
    }

    private fun extractQuery(altText: String): String {
        if (altText.isBlank()) return "Wallpaper"
        val words = altText.split(" ", ",").map { it.trim() }.filter { it.length > 3 }
        if (words.isEmpty()) return "Wallpaper"
        return words.takeLast(2).joinToString(" ")
    }
}
