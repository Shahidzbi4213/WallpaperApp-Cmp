package com.google.wallpaperapp.core.platform

import com.google.wallpaperapp.data.local.ScreenyDatabase
import com.google.wallpaperapp.data.remote.PexelWallpapersApi
import com.google.wallpaperapp.di.initKoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.koin.core.context.stopKoin
import org.koin.mp.KoinPlatform

/**
 * Smoke test for the two pieces most likely to break on a new JVM target: Room's bundled SQLite
 * (which ships per-OS natives) and the Ktor OkHttp engine. If these pass, the shared layer runs
 * on desktop.
 */
class DesktopWiringTest {

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `room opens on jvm and the seeded preference row exists`() = runBlocking {
        initKoin()
        val db = KoinPlatform.getKoin().get<ScreenyDatabase>()

        // DbModule seeds exactly one user_preference row in its onCreate callback.
        val prefs = db.userPreferenceDao().getUserPreference().first()
        assertTrue(prefs != null, "the seeded user_preference row should be readable on desktop")
        assertEquals("en", prefs.languageCode)

        assertTrue(
            java.io.File(appDataDir(), ScreenyDatabase.SCREENY_DATABASE).exists(),
            "the database file should be created under the desktop app data dir"
        )
    }

    @Test
    fun `ktor okhttp engine reaches pexels and parses a page`() = runBlocking {
        initKoin()
        val api = KoinPlatform.getKoin().get<PexelWallpapersApi>()

        val response = api.getWallpapers(page = 1)
        assertTrue(response.wallpapers.isNotEmpty(), "curated page 1 should return photos")
        assertTrue(
            response.wallpapers.first().wallpaperSource.portrait.isNotBlank(),
            "the portrait url should deserialize"
        )
    }
}
