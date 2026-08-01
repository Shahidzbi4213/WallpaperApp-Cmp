package com.google.wallpaperapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.wallpaperapp.data.paging.SearchWallpapersPagingSource
import com.google.wallpaperapp.data.remote.provider.WallpaperAggregator
import com.google.wallpaperapp.data.utils.PagingDefaults
import com.google.wallpaperapp.domain.models.Wallpaper
import kotlinx.coroutines.flow.Flow

class SearchWallpapersRepository(private val aggregator: WallpaperAggregator) {

    fun getSearchWallpapers(query: String): Flow<PagingData<Wallpaper>> = Pager(
        config = PagingConfig(
            pageSize = PagingDefaults.PAGE_SIZE,
            initialLoadSize = PagingDefaults.PAGE_SIZE,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = { SearchWallpapersPagingSource(aggregator, query) },
    ).flow
}
