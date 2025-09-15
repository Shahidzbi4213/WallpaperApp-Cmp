package com.google.wallpaperapp.ui.screens.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.wallpaperapp.data.repositories.SearchWallpapersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

class CategoryViewModel(private val repo: SearchWallpapersRepository) : ViewModel() {


   private val _query  = MutableStateFlow<String?>(null)

      @OptIn(ExperimentalCoroutinesApi::class)
      val wallpapers =   _query.filter { it != null }.flatMapLatest {
          repo.getSearchWallpapers(it!!)
      }  .cachedIn(viewModelScope)




    fun updateQuery(query: String?){
        _query.update { query}
    }

}