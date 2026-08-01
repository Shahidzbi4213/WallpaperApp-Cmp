package com.google.wallpaperapp.data.remote.provider

import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.WallpaperSource
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WallpaperAggregatorTest {

    private class FakeProvider(
        override val source: WallpaperSource,
        override val isEnabled: Boolean = true,
        private val hasMore: Boolean = true,
        private val error: Throwable? = null,
        private val count: Int = 2,
    ) : WallpaperProvider {

        var calls = 0
            private set

        private fun page(): ProviderPage {
            calls++
            error?.let { throw it }
            return ProviderPage(
                wallpapers = List(count) {
                    Wallpaper(
                        id = source.idOf("$it"),
                        source = source,
                        thumbUrl = "t", previewUrl = "p", fullUrl = "f",
                    )
                },
                hasMore = hasMore,
            )
        }

        override suspend fun featured(page: Int) = page()
        override suspend fun search(query: String, page: Int) = page()
    }

    @Test
    fun `blends results from every provider`() = runTest {
        val aggregator = WallpaperAggregator(
            listOf(
                FakeProvider(WallpaperSource.WALLHAVEN),
                FakeProvider(WallpaperSource.PEXELS),
            )
        )

        val result = aggregator.featured(1)

        assertEquals(4, result.wallpapers.size)
        assertEquals(
            setOf(WallpaperSource.WALLHAVEN, WallpaperSource.PEXELS),
            result.wallpapers.map { it.source }.toSet(),
        )
    }

    @Test
    fun `one failing provider does not empty the feed`() = runTest {
        val healthy = FakeProvider(WallpaperSource.WALLHAVEN)
        val aggregator = WallpaperAggregator(
            listOf(
                healthy,
                FakeProvider(WallpaperSource.PEXELS, error = IllegalStateException("boom")),
            )
        )

        val result = aggregator.featured(1)

        assertEquals(2, result.wallpapers.size)
        assertTrue(result.wallpapers.all { it.source == WallpaperSource.WALLHAVEN })
    }

    @Test
    fun `throws only when every provider fails`() = runTest {
        val aggregator = WallpaperAggregator(
            listOf(
                FakeProvider(WallpaperSource.WALLHAVEN, error = IllegalStateException("a")),
                FakeProvider(WallpaperSource.PEXELS, error = IllegalStateException("b")),
            )
        )

        assertFailsWith<IllegalStateException> { aggregator.featured(1) }
    }

    @Test
    fun `skips disabled providers`() = runTest {
        val disabled = FakeProvider(WallpaperSource.PEXELS, isEnabled = false)
        val aggregator = WallpaperAggregator(
            listOf(FakeProvider(WallpaperSource.WALLHAVEN), disabled)
        )

        aggregator.featured(1)

        assertEquals(0, disabled.calls)
    }

    @Test
    fun `stops calling a provider once it reports no more pages`() = runTest {
        val shallow = FakeProvider(WallpaperSource.BING, hasMore = false)
        val deep = FakeProvider(WallpaperSource.WALLHAVEN, hasMore = true)
        val aggregator = WallpaperAggregator(listOf(deep, shallow))

        aggregator.featured(1)
        aggregator.featured(2)

        assertEquals(1, shallow.calls)
        assertEquals(2, deep.calls)
    }

    @Test
    fun `exhaustion on the home feed does not affect a search`() = runTest {
        val shallow = FakeProvider(WallpaperSource.BING, hasMore = false)
        val aggregator = WallpaperAggregator(listOf(shallow))

        aggregator.featured(1)
        // Home feed is done with this provider, but a search is a separate feed.
        val searchResult = aggregator.search("mountains", 1)

        assertEquals(2, shallow.calls)
        assertEquals(2, searchResult.wallpapers.size)
    }

    @Test
    fun `restarting a feed at page one clears exhaustion`() = runTest {
        val shallow = FakeProvider(WallpaperSource.BING, hasMore = false)
        val aggregator = WallpaperAggregator(listOf(shallow))

        aggregator.featured(1)
        aggregator.featured(2)
        aggregator.featured(1)

        // Page 2 skipped it; the refresh back to page 1 brought it back.
        assertEquals(2, shallow.calls)
    }

    @Test
    fun `reports no more pages when all providers are exhausted`() = runTest {
        val aggregator = WallpaperAggregator(
            listOf(FakeProvider(WallpaperSource.WALLHAVEN, hasMore = false))
        )

        assertFalse(aggregator.featured(1).hasMore)
    }

    @Test
    fun `returns empty rather than throwing when no provider is available`() = runTest {
        val aggregator = WallpaperAggregator(
            listOf(FakeProvider(WallpaperSource.PEXELS, isEnabled = false))
        )

        val result = aggregator.featured(1)

        assertTrue(result.wallpapers.isEmpty())
        assertFalse(result.hasMore)
    }
}
