package com.google.wallpaperapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.wallpaperapp.data.paging.SearchWallpapersPagingSource
import com.google.wallpaperapp.data.remote.PexelWallpapersApi
import com.google.wallpaperapp.data.utils.Constant
import com.google.wallpaperapp.domain.mappers.toWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchWallpapersRepository(private val api: PexelWallpapersApi) {

    fun getSearchWallpapers(query: String): Flow<PagingData<Wallpaper>>  {

        val pageConfig = PagingConfig(
            pageSize = Constant.PER_PAGE_ITEMS,
            initialLoadSize = Constant.PER_PAGE_ITEMS * 2,
            enablePlaceholders = false
        )
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { SearchWallpapersPagingSource(api, query) },
        ).flow.map { pagingData ->
            pagingData.map { wallpaperResponse -> wallpaperResponse.toWallpaper()}
        }
    }
}
