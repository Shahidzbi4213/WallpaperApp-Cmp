package com.google.wallpaperapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.wallpaperapp.data.remote.PexelWallpapersApi
import com.google.wallpaperapp.data.remote.models.WallpaperResponse


class SearchWallpapersPagingSource(
    private val api: PexelWallpapersApi,
    private val query: String,
    private val orientation: String? = null
) : PagingSource<Int, WallpaperResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WallpaperResponse> {
        try {

            if (query.isEmpty()) {
                return LoadResult.Error(Throwable("Query is Empty"))
            }

            val position = params.key ?: 1
            val response = api.searchWallpaper(position, query, orientation = orientation)
            val filteredWallpapers = if (orientation == "landscape") {
                response.wallpapers.filter { it.width == 0 || it.height == 0 || it.width >= it.height }
            } else {
                response.wallpapers
            }
            return LoadResult.Page(
                data = filteredWallpapers.shuffled(),
                prevKey = if (response.prevPage == null) null else position - 1,
                nextKey = if (response.nextPage == null) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, WallpaperResponse>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }


}