package com.google.wallpaperapp.di


import com.google.wallpaperapp.core.platform.HttpEngineFactory
import com.google.wallpaperapp.data.remote.provider.BingDailyProvider
import com.google.wallpaperapp.data.remote.provider.PexelsProvider
import com.google.wallpaperapp.data.remote.provider.ProviderHealth
import com.google.wallpaperapp.data.remote.provider.WallhavenProvider
import com.google.wallpaperapp.data.remote.provider.WallpaperAggregator
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
class NetworkModule {

    private val TIME_OUT = 10_000L

    @Single
    fun provideHttpClient(engine: HttpClientEngine): HttpClient = HttpClient(engine) {

        // Without this a 429 would not throw; `.body()` would instead try to
        // deserialize the error page and fail with a confusing parse error,
        // and ProviderHealth would never see a rate-limit response.
        expectSuccess = true

        install(HttpTimeout) {
            connectTimeoutMillis = TIME_OUT
            requestTimeoutMillis = TIME_OUT
            socketTimeoutMillis = TIME_OUT
        }

        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 2)
            // 429 is not a server error, so it needs calling out explicitly.
            retryIf(maxRetries = 2) { _, response ->
                response.status == HttpStatusCode.TooManyRequests
            }
            exponentialDelay()
        }

        // No Authorization header here. Auth is per-provider — a global one
        // would send the Pexels key to every other provider's host.
        install(DefaultRequest) {
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
    }

    @Factory
    fun provideEngine(): HttpClientEngine = HttpEngineFactory().getHttpClientEngine()

    @Single
    fun providePexelsProvider(httpClient: HttpClient) = PexelsProvider(httpClient)

    @Single
    fun provideWallhavenProvider(httpClient: HttpClient) = WallhavenProvider(httpClient)

    @Single
    fun provideBingDailyProvider(httpClient: HttpClient) = BingDailyProvider(httpClient)

    @Single
    fun provideProviderHealth() = ProviderHealth()

    /**
     * List order sets the interleave order of the blended feed. Wallhaven leads
     * because it is the only source filtered to phone aspect ratios, and the
     * only one that works with no API key configured.
     */
    @Single
    fun provideWallpaperAggregator(
        wallhaven: WallhavenProvider,
        pexels: PexelsProvider,
        bing: BingDailyProvider,
        health: ProviderHealth,
    ) = WallpaperAggregator(
        providers = listOf(wallhaven, pexels, bing),
        health = health,
    )
}
