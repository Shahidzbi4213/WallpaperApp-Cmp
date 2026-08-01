package com.google.wallpaperapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.google.wallpaperapp.data.local.dao.CommonDao
import com.google.wallpaperapp.data.local.dao.WallpaperDao
import com.google.wallpaperapp.data.local.dao.WallpaperRemoteKeysDao
import com.google.wallpaperapp.data.local.entities.WallpaperEntity
import com.google.wallpaperapp.data.local.entities.WallpaperRemoteKeyEntity
import com.google.wallpaperapp.data.remote.provider.WallpaperAggregator
import com.google.wallpaperapp.domain.mappers.toWallpaperEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * Backs the home feed. Each page is fetched from every enabled provider at once
 * and blended by the [WallpaperAggregator] before being written to Room.
 */
@OptIn(ExperimentalPagingApi::class)
class WallpaperRemoteMediator(
    private val wallpaperDao: WallpaperDao,
    private val remoteKeysDao: WallpaperRemoteKeysDao,
    private val commonDao: CommonDao,
    private val aggregator: WallpaperAggregator,
) : RemoteMediator<Int, WallpaperEntity>() {

    private var lastCalledPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, WallpaperEntity>,
    ): MediatorResult = withContext(Dispatchers.IO) {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND ->
                    return@withContext MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> lastCalledPage + 1
            }

            val result = aggregator.featured(page)
            val endOfPaginationReached = !result.hasMore || result.wallpapers.isEmpty()
            lastCalledPage = page

            if (loadType == LoadType.REFRESH) {
                commonDao.clearAllWallpapers()
            }

            val prevPage = if (page > 1) page - 1 else null
            val nextPage = if (endOfPaginationReached) null else page + 1

            remoteKeysDao.addAllRemoteKeys(
                result.wallpapers.map { wallpaper ->
                    WallpaperRemoteKeyEntity(wallpaper.id, prevPage, nextPage, page)
                }
            )
            wallpaperDao.addWallpapers(result.wallpapers.map { it.toWallpaperEntity(page) })

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
