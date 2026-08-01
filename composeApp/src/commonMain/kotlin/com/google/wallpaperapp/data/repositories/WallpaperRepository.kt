package com.google.wallpaperapp.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.wallpaperapp.data.local.dao.CommonDao
import com.google.wallpaperapp.data.local.dao.WallpaperDao
import com.google.wallpaperapp.data.local.dao.WallpaperRemoteKeysDao
import com.google.wallpaperapp.data.local.entities.WallpaperEntity
import com.google.wallpaperapp.data.paging.WallpaperRemoteMediator
import com.google.wallpaperapp.data.remote.provider.WallpaperAggregator
import com.google.wallpaperapp.data.utils.PagingDefaults


@OptIn(ExperimentalPagingApi::class)
class WallpaperRepository(
    private val wallpaperDao: WallpaperDao,
    private val remoteKeysDao: WallpaperRemoteKeysDao,
    private val commonDao: CommonDao,
    private val aggregator: WallpaperAggregator,
) {
    fun getAllWallpapers(): Pager<Int, WallpaperEntity> = Pager(
        config = PagingConfig(pageSize = PagingDefaults.PAGE_SIZE),
        remoteMediator = WallpaperRemoteMediator(
            wallpaperDao, remoteKeysDao, commonDao, aggregator
        ),
        pagingSourceFactory = { wallpaperDao.getAllWallpapers() },
    )
}
