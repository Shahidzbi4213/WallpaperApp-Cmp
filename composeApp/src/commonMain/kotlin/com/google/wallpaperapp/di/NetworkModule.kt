package com.google.wallpaperapp.di


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
                header(HttpHeaders.Authorization,"563492ad6f917000010000013c35869795db4034972b1408c54283c4")
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

