package com.google.wallpaperapp.data.remote.provider

import com.google.wallpaperapp.domain.models.WallpaperSource
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.Duration.Companion.minutes

/**
 * Tracks which providers are being rate-limited so the aggregator can skip them
 * instead of spending every page load on a request that will fail.
 *
 * Providers have very different budgets — Wallhaven allows 45/minute while an
 * Unsplash demo key allows 50/hour — so one exhausted provider must not stall
 * the whole feed.
 */
@OptIn(ExperimentalTime::class)
class ProviderHealth(private val cooldown: Duration = DEFAULT_COOLDOWN) {

    private val coolingOff = mutableMapOf<WallpaperSource, Instant>()

    fun isAvailable(source: WallpaperSource): Boolean {
        val until = coolingOff[source] ?: return true
        if (Clock.System.now() >= until) {
            coolingOff.remove(source)
            return true
        }
        return false
    }

    /** Call with whatever a provider threw; only rate-limit failures are recorded. */
    fun recordFailure(source: WallpaperSource, error: Throwable) {
        if (isRateLimit(error)) {
            coolingOff[source] = Clock.System.now() + cooldown
        }
    }

    fun recordSuccess(source: WallpaperSource) {
        coolingOff.remove(source)
    }

    private fun isRateLimit(error: Throwable): Boolean {
        val status = (error as? ResponseException)?.response?.status ?: return false
        // Unsplash answers an exhausted quota with 403 rather than 429.
        return status == HttpStatusCode.TooManyRequests || status == HttpStatusCode.Forbidden
    }

    companion object {
        val DEFAULT_COOLDOWN: Duration = 5.minutes
    }
}
