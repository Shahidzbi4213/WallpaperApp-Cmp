package com.google.wallpaperapp.di


import com.google.wallpaperapp.AppConfig
import com.google.wallpaperapp.core.platform.HttpEngineFactory
import com.google.wallpaperapp.data.remote.PexelWallpapersApi
import com.google.wallpaperapp.data.remote.PexelWallpapersApiImpl
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingFormat
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
class NetworkModule {
  private  val TIME_OUT = 10_00_0L

    @Single
    fun provideHttpClient(engine: HttpClientEngine): HttpClient {

        return HttpClient(engine){

            // Without this, a 401/429/500 body is handed straight to the WallpaperMainResponse
            // deserializer, which then reports "fields are required" -- an error that looks like a
            // schema bug and hides the real cause. Non-2xx now throws with the actual status.
            expectSuccess = true

            // Retries cover genuinely transient failures only. 401 is deliberately NOT retried:
            // a rejected key is rejected on every attempt, so retrying only delays the error.
            install(HttpRequestRetry) {
                maxRetries = 3
                // One predicate on purpose: retryIf REPLACES whatever retryOnServerErrors set, so
                // combining the two silently disables server-error retries.
                retryIf { _, response ->
                    response.status.value >= 500 ||
                        response.status == HttpStatusCode.TooManyRequests
                }
                // Keep the worst case a few seconds; the default base backs off far enough that
                // a failing page feels like a hang.
                exponentialDelay(base = 1.5, maxDelayMs = 2000)
            }

            install(HttpTimeout){
                connectTimeoutMillis = TIME_OUT
                requestTimeoutMillis = TIME_OUT
                socketTimeoutMillis = TIME_OUT
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })

            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Authorization, AppConfig.PEXELS_API_KEY)
            }

        }
    }

    @Factory
    fun provideEngine(): HttpClientEngine {
        return HttpEngineFactory().getHttpClientEngine()
    }

    @Single(binds = [PexelWallpapersApi::class])
    fun provideWallpaperApi(
        httpClient: HttpClient
    ): PexelWallpapersApiImpl = PexelWallpapersApiImpl(httpClient = httpClient)
}

