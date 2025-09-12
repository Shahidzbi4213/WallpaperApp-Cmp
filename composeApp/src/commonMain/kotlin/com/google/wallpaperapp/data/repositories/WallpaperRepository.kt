package com.google.wallpaperapp.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.wallpaperapp.data.utils.Constant
import com.google.wallpaperapp.data.local.dao.PexelWallpaperDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperRemoteKeysDao
import com.google.wallpaperapp.data.paging.PexelWallpaperRemoteMediator
import com.google.wallpaperapp.data.remote.PexelWallpapersApi
import com.google.wallpaperapp.domain.mappers.toWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


@OptIn(ExperimentalPagingApi::class)
class WallpaperRepository(
    private val wallpaperDao: PexelWallpaperDao,
    private val remoteKeysDao: PexelWallpaperRemoteKeysDao,
    private val api: PexelWallpapersApi
) {
    fun getAllWallpapers(): Flow<PagingData<Wallpaper>> {
        val pageConfig = PagingConfig(
            pageSize = Constant.PER_PAGE_ITEMS,
            initialLoadSize = Constant.PER_PAGE_ITEMS,
            prefetchDistance = 10,
            enablePlaceholders = false
        )

        return Pager(
            config = pageConfig,
            remoteMediator = PexelWallpaperRemoteMediator(wallpaperDao, remoteKeysDao, api),
            pagingSourceFactory = { wallpaperDao.getAllWallpapers() }
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toWallpaper() }
        }
    }
}
