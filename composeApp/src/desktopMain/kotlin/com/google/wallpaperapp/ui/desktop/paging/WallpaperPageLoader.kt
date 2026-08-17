package com.google.wallpaperapp.ui.desktop.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.google.wallpaperapp.core.platform.AppLogger
import com.google.wallpaperapp.data.remote.PexelWallpapersApi
import com.google.wallpaperapp.data.utils.toUserMessage
import com.google.wallpaperapp.domain.mappers.toWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.koin.mp.KoinPlatform
import kotlin.math.ceil
import kotlin.math.min

/** Desktop shows 40 to a page: about 1.5 screens, so a page is scanned rather than scrolled. */
const val DESKTOP_PER_PAGE = 40

/** Which feed to page through. */
@Immutable
sealed interface WallpaperFeed {
    @Immutable
    data object Curated : WallpaperFeed

    @Immutable
    data class Search(val query: String) : WallpaperFeed
}

@Immutable
data class PageState(
    val items: List<Wallpaper> = emptyList(),
    val page: Int = 1,
    val totalPages: Int = 1,
    val isLoading: Boolean = false,
    val error: StringResource? = null
) {
    val isEmpty: Boolean get() = !isLoading && error == null && items.isEmpty()
}

/**
 * Numbered pagination for the desktop grids.
 *
 * Deliberately not Paging 3. Paging models an endlessly appending list, and the mobile app wants
 * exactly that -- but the Pexels endpoints are already page-indexed and return `total_results`,
 * so for discrete pages we can just ask for page N. Bending Paging into random-access jumps
 * would be more code and more fragile than this.
 *
 * Mobile keeps its Paging 3 path untouched.
 */
@Stable
class WallpaperPageLoader(
    private val api: PexelWallpapersApi,
    private val scope: CoroutineScope,
    private val perPage: Int = DESKTOP_PER_PAGE
) {
    var state by mutableStateOf(PageState())
        private set

    private var feed: WallpaperFeed = WallpaperFeed.Curated
    private var job: Job? = null

    /**
     * The deepest page this feed actually served, once we have proof total_results was optimistic.
     * Sticky for the life of the feed -- otherwise the next successful fetch recomputes the total
     * from total_results and walks the user straight back into the dead page.
     */
    private var discoveredLastPage: Int? = null

    /**
     * Pages already fetched this session, so paging back to 1 is instant and does not spend
     * another request. Bounded because a long session could otherwise hold thousands of images.
     */
    private val cache = object : LinkedHashMap<String, List<Wallpaper>>(0, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, List<Wallpaper>>) =
            size > MAX_CACHED_PAGES
    }

    /**
     * Page count belongs to the feed, not to any one page. Kept out of the cache so a page
     * fetched before the depth was clamped cannot restore a stale, too-large total.
     */
    private var feedTotalPages: Int = 1

    /** Point the loader at a feed. Resets to page 1 unless it is already showing this feed. */
    fun setFeed(newFeed: WallpaperFeed) {
        if (feed == newFeed && (state.items.isNotEmpty() || state.isLoading)) return
        feed = newFeed
        discoveredLastPage = null
        feedTotalPages = 1
        goToPage(1)
    }

    fun goToPage(page: Int) {
        val target = page.coerceAtLeast(1)
        job?.cancel()

        cache[keyFor(feed, target)]?.let { cached ->
            state = PageState(cached, target, feedTotalPages)
            return
        }

        state = state.copy(page = target, isLoading = true, error = null)

        job = scope.launch {
            runCatching { fetch(feed, target) }
                .onSuccess { response ->
                    val items = response.wallpapers.map { it.toWallpaper() }

                    if (items.isEmpty() && target > 1) {
                        // total_results overstated the real depth; clamp and step back so the
                        // user never lands on a dead page they cannot navigate out of.
                        val lastGood = target - 1
                        discoveredLastPage = lastGood
                        feedTotalPages = lastGood
                        state = state.copy(totalPages = lastGood, isLoading = false)
                        goToPage(lastGood)
                        return@onSuccess
                    }

                    feedTotalPages = totalPagesFrom(response.totalResults)
                    cache[keyFor(feed, target)] = items
                    state = PageState(items, target, feedTotalPages)
                }
                .onFailure { throwable ->
                    if (throwable is kotlinx.coroutines.CancellationException) throw throwable
                    AppLogger.e(TAG, "page $target of $feed failed: ${throwable::class.simpleName}: ${throwable.message}", throwable)
                    state = state.copy(isLoading = false, error = throwable.toUserMessage())
                }
        }
    }

    fun retry() = goToPage(state.page)

    fun next() {
        if (state.page < state.totalPages) goToPage(state.page + 1)
    }

    fun previous() {
        if (state.page > 1) goToPage(state.page - 1)
    }

    private suspend fun fetch(feed: WallpaperFeed, page: Int) = when (feed) {
        is WallpaperFeed.Curated -> api.getWallpapers(page = page, perPage = perPage)
        is WallpaperFeed.Search -> api.searchWallpaper(page = page, query = feed.query, perPage = perPage)
    }

    private fun totalPagesFrom(totalResults: Int): Int {
        if (totalResults <= 0) return discoveredLastPage ?: 1
        val claimed = ceil(totalResults.toDouble() / perPage).toInt()
        val capped = min(claimed, discoveredLastPage ?: MAX_PAGES)
        return min(capped, MAX_PAGES).coerceAtLeast(1)
    }

    private fun keyFor(feed: WallpaperFeed, page: Int) = when (feed) {
        is WallpaperFeed.Curated -> "curated:$page"
        is WallpaperFeed.Search -> "search:${feed.query}:$page"
    }

    private companion object {
        const val TAG = "WallpaperPageLoader"
        const val MAX_CACHED_PAGES = 16

        /**
         * A hard ceiling regardless of what total_results claims. Pexels caps search at 8000
         * results, and curated reports ~66k but is not guaranteed to serve arbitrarily deep
         * pages; an empty response clamps further at runtime.
         */
        const val MAX_PAGES = 2000
    }
}

@Composable
fun rememberWallpaperPageLoader(
    perPage: Int = DESKTOP_PER_PAGE
): WallpaperPageLoader {
    val scope = rememberCoroutineScope()
    return remember(scope, perPage) {
        WallpaperPageLoader(KoinPlatform.getKoin().get(), scope, perPage)
    }
}
