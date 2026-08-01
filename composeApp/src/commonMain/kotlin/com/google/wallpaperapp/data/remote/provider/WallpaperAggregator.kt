package com.google.wallpaperapp.data.remote.provider

import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.WallpaperSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Fans a page request out to every enabled provider in parallel and blends the
 * results into a single feed.
 *
 * Two properties matter here:
 *  - **Partial failure is survivable.** If one provider is down, throttled or
 *    misconfigured, its results are dropped and the feed still loads. Only a
 *    total failure propagates as an error.
 *  - **Ordering is deterministic.** Results are interleaved round-robin rather
 *    than shuffled, so the grid mixes sources evenly and a reload does not
 *    reorder everything under the user.
 */
class WallpaperAggregator(
    private val providers: List<WallpaperProvider>,
    private val health: ProviderHealth = ProviderHealth(),
) {

    /**
     * Providers that have run out of pages, tracked per feed.
     *
     * This is keyed rather than held as one flat set because a single
     * aggregator instance is shared between the home feed and every search:
     * Bing running out on the home feed must not remove Pexels' or Wallhaven's
     * results from an unrelated search.
     */
    private val exhausted = mutableMapOf<String, MutableSet<WallpaperSource>>()

    suspend fun featured(page: Int): ProviderPage =
        fanOut(feedKey = FEATURED_KEY, page = page) { provider -> provider.featured(page) }

    suspend fun search(query: String, page: Int): ProviderPage =
        fanOut(feedKey = "search:$query", page = page) { provider -> provider.search(query, page) }

    private suspend fun fanOut(
        feedKey: String,
        page: Int,
        call: suspend (WallpaperProvider) -> ProviderPage,
    ): ProviderPage = coroutineScope {
        // Page 1 is a fresh start for this feed, so forget what was exhausted.
        if (page <= 1) exhausted.remove(feedKey)
        val done = exhausted.getOrPut(feedKey) { mutableSetOf() }

        val active = providers.filter {
            it.isEnabled && it.source !in done && health.isAvailable(it.source)
        }
        if (active.isEmpty()) return@coroutineScope ProviderPage.Empty

        val results = active
            .map { provider -> async { provider.source to runCatching { call(provider) } } }
            .awaitAll()

        results.forEach { (source, result) ->
            result.fold(
                onSuccess = { providerPage ->
                    health.recordSuccess(source)
                    if (!providerPage.hasMore) done += source
                },
                onFailure = { error -> health.recordFailure(source, error) },
            )
        }

        // Surface an error only when nothing at all came back, so one failing
        // provider cannot empty the screen.
        val pages = results.mapNotNull { (_, result) -> result.getOrNull() }
        if (pages.isEmpty()) {
            throw results.first().second.exceptionOrNull()
                ?: IllegalStateException("No wallpaper provider returned a result")
        }

        ProviderPage(
            wallpapers = interleave(pages.map { it.wallpapers }),
            hasMore = pages.any { it.hasMore },
        )
    }

    private companion object {
        const val FEATURED_KEY = "featured"
    }
}

/**
 * Round-robin merge: one item from each list in turn, skipping lists as they run
 * out. Providers return different page sizes (Wallhaven is fixed at 24, Bing
 * yields 8 once), so the tail is naturally dominated by the deeper sources.
 */
internal fun interleave(lists: List<List<Wallpaper>>): List<Wallpaper> {
    val nonEmpty = lists.filter { it.isNotEmpty() }
    if (nonEmpty.size <= 1) return nonEmpty.flatten()

    val merged = ArrayList<Wallpaper>(nonEmpty.sumOf { it.size })
    val seen = HashSet<String>()
    val longest = nonEmpty.maxOf { it.size }
    var index = 0
    while (index < longest) {
        for (list in nonEmpty) {
            val item = list.getOrNull(index) ?: continue
            if (seen.add(item.id)) merged += item
        }
        index++
    }
    return merged
}
