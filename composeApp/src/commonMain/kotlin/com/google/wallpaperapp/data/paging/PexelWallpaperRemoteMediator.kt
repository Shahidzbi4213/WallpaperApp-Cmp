package com.google.wallpaperapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.google.wallpaperapp.data.local.dao.CommonDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperDao
import com.google.wallpaperapp.data.local.dao.PexelWallpaperRemoteKeysDao
import com.google.wallpaperapp.data.local.entities.WallpaperEntity
import com.google.wallpaperapp.data.local.entities.WallpaperRemoteKeyEntity
import com.google.wallpaperapp.data.remote.PexelWallpapersApi
import com.google.wallpaperapp.domain.mappers.toWallpaperEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class PexelWallpaperRemoteMediator(
    private val wallpaperDao: PexelWallpaperDao,
    private val remoteKeysDao: PexelWallpaperRemoteKeysDao,
    private val commonDao: CommonDao,
    private val pexelWallpapersApi: PexelWallpapersApi
) : RemoteMediator<Int, WallpaperEntity>() {


    override suspend fun load(loadType: LoadType, state: PagingState<Int, WallpaperEntity>): MediatorResult {
        return withContext(Dispatchers.IO) {
            try {

                val page = when (loadType) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                        remoteKeys?.nextPage?.minus(1) ?: 1
                    }

                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                        val prevPage = remoteKeys?.prevPage
                        prevPage ?: 1
                    }

                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                        val nextPage = remoteKeys?.nextPage
                        nextPage ?: return@withContext MediatorResult.Success(endOfPaginationReached = true)
                    }
                }

                val response = pexelWallpapersApi.getWallpapers(page = page)
                val endOfPaginationReached = response.nextPage == null


                withContext(Dispatchers.IO) {
                    if (loadType == LoadType.REFRESH) {
                        commonDao.clearAllWallpapers()
                    }

                    val prevPage = if (page > 1) page - 1 else if (page == 1) 1 else null
                    val nextPage = if (endOfPaginationReached) null else page + 1

                    val keys = response.wallpapers.map { wallpaper ->
                        WallpaperRemoteKeyEntity(wallpaperId = wallpaper.id, prevPage, nextPage, page)
                    }
                    remoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                    wallpaperDao.addWallpapers(
                        response.wallpapers
                        .onEach { wallpaper -> wallpaper.page = page }
                        .map { it.toWallpaperEntity() }
                    )
                }

                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } catch (e: Exception) {
                MediatorResult.Error(e)
            }
        }
    }

    /**
     * Retrieves the remote key for the item closest to the user's current scroll position.
     *
     * @param state The current state of the paging system, which includes the position of items loaded.
     * @return The corresponding remote key for the item closest to the current scroll position, or null if not found.
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, WallpaperEntity>
    ): WallpaperRemoteKeyEntity? {

        // Get the user's current position in the list (anchorPosition).
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->

                // Find the Wallpaper closest to that position.
                state.closestItemToPosition(position)?.id?.let { id ->

                    // Use the wallpaper's ID to retrieve the corresponding remote key from the database.
                    remoteKeysDao.getRemoteKeyByWallpaperId(id = id)
                }
            }
        }
    }

    /**
     * Retrieves the remote key for the first item in the loaded data pages.
     *
     * @param state The current state of the paging system, which includes the pages of items loaded.
     * @return The corresponding remote key for the first item in the first loaded page, or null if not found.
     */
    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, WallpaperEntity>
    ): WallpaperRemoteKeyEntity? {

        // Find the first page that contains data (not empty).
        return withContext(Dispatchers.IO) {
            state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { wallpaper ->

                // Use the first wallpaper's ID to retrieve the corresponding remote key from the database.
                remoteKeysDao.getRemoteKeyByWallpaperId(id = wallpaper.id)
            }
        }
    }


    /**
     * Retrieves the remote key for the last item in the loaded data pages.
     *
     * @param state The current state of the paging system, which includes the pages of items loaded.
     * @return The corresponding remote key for the last item in the last loaded page, or null if not found.
     */
    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, WallpaperEntity>
    ): WallpaperRemoteKeyEntity? {

        // Find the last page that contains data (not empty).
        return withContext(Dispatchers.IO) {
            state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { wallpaper ->

                // Use the last Wallpaper's ID to retrieve the corresponding remote key from the database.
                remoteKeysDao.getRemoteKeyByWallpaperId(id = wallpaper.id)
            }
        }
    }

}