package com.google.wallpaperapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.wallpaperapp.data.remote.provider.WallpaperAggregator
import com.google.wallpaperapp.domain.models.Wallpaper


class SearchWallpapersPagingSource(
    private val aggregator: WallpaperAggregator,
    private val query: String,
) : PagingSource<Int, Wallpaper>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wallpaper> {
        if (query.isBlank()) {
            return LoadResult.Error(IllegalArgumentException("Query is empty"))
        }

        return try {
            val position = params.key ?: 1
            val result = aggregator.search(query, position)
            LoadResult.Page(
                // The aggregator already interleaves providers, so results arrive
                // evenly mixed without the reshuffle-on-every-load this used to do.
                data = result.wallpapers,
                prevKey = if (position <= 1) null else position - 1,
                nextKey = if (result.hasMore && result.wallpapers.isNotEmpty()) position + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Wallpaper>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
}
