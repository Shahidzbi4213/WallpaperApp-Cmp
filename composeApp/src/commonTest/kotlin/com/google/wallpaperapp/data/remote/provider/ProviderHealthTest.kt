package com.google.wallpaperapp.data.remote.provider

import com.google.wallpaperapp.domain.models.WallpaperSource
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes

class ProviderHealthTest {

    @Test
    fun `providers start available`() {
        val health = ProviderHealth()

        assertTrue(health.isAvailable(WallpaperSource.PEXELS))
    }

    @Test
    fun `a non-rate-limit failure does not sideline a provider`() {
        val health = ProviderHealth(cooldown = 5.minutes)

        health.recordFailure(WallpaperSource.PEXELS, IllegalStateException("socket closed"))

        // A transient network blip should not stop us trying again next page.
        assertTrue(health.isAvailable(WallpaperSource.PEXELS))
    }

    @Test
    fun `success clears any cooldown`() {
        val health = ProviderHealth(cooldown = 5.minutes)

        health.recordSuccess(WallpaperSource.WALLHAVEN)

        assertTrue(health.isAvailable(WallpaperSource.WALLHAVEN))
    }

    @Test
    fun `a zero cooldown expires immediately`() {
        val health = ProviderHealth(cooldown = kotlin.time.Duration.ZERO)

        health.recordFailure(WallpaperSource.PEXELS, IllegalStateException("boom"))

        assertTrue(health.isAvailable(WallpaperSource.PEXELS))
    }

    @Test
    fun `one provider failing leaves the others alone`() {
        val health = ProviderHealth(cooldown = 5.minutes)

        health.recordFailure(WallpaperSource.PEXELS, IllegalStateException("boom"))

        assertTrue(health.isAvailable(WallpaperSource.WALLHAVEN))
        assertTrue(health.isAvailable(WallpaperSource.BING))
    }
}
