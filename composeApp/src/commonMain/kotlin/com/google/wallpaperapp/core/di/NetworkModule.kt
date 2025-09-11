package com.google.wallpaperapp.core.di


import com.google.wallpaperapp.core.platform.HttpEngineFactory
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
                header(HttpHeaders.Authorization,"")
            }

        }
    }

    @Factory
    fun provideEngine(): HttpClientEngine {
        return HttpEngineFactory().getHttpClientEngine()
    }
}

