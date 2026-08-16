package com.google.wallpaperapp.ui.desktop.paging

import com.google.wallpaperapp.data.remote.PexelWallpapersApi
import com.google.wallpaperapp.data.remote.models.SrcResponse
import com.google.wallpaperapp.data.remote.models.WallpaperMainResponse
import com.google.wallpaperapp.data.remote.models.WallpaperResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WallpaperPageLoaderTest {

    /** Records every call so the cache can be proven to actually prevent requests. */
    private class FakeApi(
        var totalResults: Int = 400,
        var emptyFromPage: Int = Int.MAX_VALUE,
        var failWith: Throwable? = null
    ) : PexelWallpapersApi {
        val calls = mutableListOf<Pair<String, Int>>()

        private fun respond(page: Int, perPage: Int): WallpaperMainResponse {
            failWith?.let { throw it }
            val photos = if (page >= emptyFromPage) emptyList() else List(perPage) {
                WallpaperResponse(
                    id = (page * 1000L) + it,
                    photographerName = "p",
                    photographerUrl = "u",
                    wallpaperSource = SrcResponse("m", "p", "s", "l", "o"),
                    alt = "alt"
                )
            }
            return WallpaperMainResponse(
                page = page, perPage = perPage, wallpapers = photos, totalResults = totalResults
            )
        }

        override suspend fun getWallpapers(page: Int, perPage: Int): WallpaperMainResponse {
            calls += "curated" to page
            return respond(page, perPage)
        }

        override suspend fun searchWallpaper(page: Int, query: String, perPage: Int): WallpaperMainResponse {
            calls += query to page
            return respond(page, perPage)
        }
    }

    private fun TestScope.loader(api: FakeApi, perPage: Int = 40) =
        WallpaperPageLoader(api, this, perPage)

    @Test
    fun `total pages is derived from total_results and page size`() = runTest {
        val api = FakeApi(totalResults = 401)
        val loader = loader(api)
        loader.setFeed(WallpaperFeed.Curated)
        testScheduler.advanceUntilIdle()

        // 401 / 40 rounds up to 11 -- a partial last page still counts.
        assertEquals(11, loader.state.totalPages)
        assertEquals(1, loader.state.page)
        assertEquals(40, loader.state.items.size)
    }

    @Test
    fun `jumping to a page fetches exactly that page`() = runTest {
        val api = FakeApi(totalResults = 4000)
        val loader = loader(api)
        loader.setFeed(WallpaperFeed.Curated)
        testScheduler.advanceUntilIdle()

        loader.goToPage(42)
        testScheduler.advanceUntilIdle()

        assertEquals(42, loader.state.page)
        assertEquals(listOf("curated" to 1, "curated" to 42), api.calls)
    }

    @Test
    fun `revisiting a page is served from cache without another request`() = runTest {
        val api = FakeApi(totalResults = 4000)
        val loader = loader(api)
        loader.setFeed(WallpaperFeed.Curated)
        testScheduler.advanceUntilIdle()
        loader.goToPage(2)
        testScheduler.advanceUntilIdle()

        val callsBefore = api.calls.size
        loader.goToPage(1)
        testScheduler.advanceUntilIdle()

        assertEquals(1, loader.state.page)
        assertEquals(40, loader.state.items.size)
        assertEquals(callsBefore, api.calls.size, "page 1 should have come from the cache")
    }

    @Test
    fun `an empty page clamps the total and steps back to the last real page`() = runTest {
        // total_results claims 100 pages but the API dries up after page 5.
        val api = FakeApi(totalResults = 4000, emptyFromPage = 6)
        val loader = loader(api)
        loader.setFeed(WallpaperFeed.Curated)
        testScheduler.advanceUntilIdle()

        loader.goToPage(6)
        testScheduler.advanceUntilIdle()

        assertEquals(5, loader.state.page, "should land on the last page that had results")
        assertEquals(5, loader.state.totalPages)
        assertTrue(loader.state.items.isNotEmpty())
    }

    @Test
    fun `a cached page does not restore a total that has since been clamped`() = runTest {
        val api = FakeApi(totalResults = 4000, emptyFromPage = 6)
        val loader = loader(api)
        loader.setFeed(WallpaperFeed.Curated)      // page 1 cached while total still reads 100
        testScheduler.advanceUntilIdle()
        assertEquals(100, loader.state.totalPages)

        loader.goToPage(6)                         // empty -> clamps to 5
        testScheduler.advanceUntilIdle()
        assertEquals(5, loader.state.totalPages)

        loader.goToPage(1)                         // served from cache
        testScheduler.advanceUntilIdle()
        assertEquals(5, loader.state.totalPages, "cache must not resurrect the pre-clamp total")
    }

    @Test
    fun `page numbers below one are clamped rather than requested`() = runTest {
        val api = FakeApi(totalResults = 4000)
        val loader = loader(api)
        loader.setFeed(WallpaperFeed.Curated)
        testScheduler.advanceUntilIdle()

        loader.goToPage(0)
        testScheduler.advanceUntilIdle()

        assertEquals(1, loader.state.page)
        assertTrue(api.calls.none { it.second < 1 }, "must never ask the API for page < 1")
    }

    @Test
    fun `next and previous respect the boundaries`() = runTest {
        val api = FakeApi(totalResults = 80)   // exactly 2 pages at 40
        val loader = loader(api)
        loader.setFeed(WallpaperFeed.Curated)
        testScheduler.advanceUntilIdle()

        loader.previous()
        testScheduler.advanceUntilIdle()
        assertEquals(1, loader.state.page, "previous on page 1 is a no-op")

        loader.next(); testScheduler.advanceUntilIdle()
        assertEquals(2, loader.state.page)

        loader.next(); testScheduler.advanceUntilIdle()
        assertEquals(2, loader.state.page, "next on the last page is a no-op")
    }

    @Test
    fun `a failure surfaces an error and retry re-requests the same page`() = runTest {
        val api = FakeApi(totalResults = 4000, failWith = IllegalStateException("boom"))
        val loader = loader(api)
        loader.setFeed(WallpaperFeed.Curated)
        testScheduler.advanceUntilIdle()

        assertNotNull(loader.state.error)
        assertEquals("boom", loader.state.error)

        api.failWith = null
        loader.retry()
        testScheduler.advanceUntilIdle()

        assertEquals(null, loader.state.error)
        assertEquals(40, loader.state.items.size)
    }

    @Test
    fun `switching search query resets to page one and keys the cache separately`() = runTest {
        val api = FakeApi(totalResults = 4000)
        val loader = loader(api)

        loader.setFeed(WallpaperFeed.Search("cats"))
        testScheduler.advanceUntilIdle()
        loader.goToPage(3)
        testScheduler.advanceUntilIdle()

        loader.setFeed(WallpaperFeed.Search("dogs"))
        testScheduler.advanceUntilIdle()

        assertEquals(1, loader.state.page, "a new query starts at page 1")
        assertEquals(listOf("cats" to 1, "cats" to 3, "dogs" to 1), api.calls)
    }
}
