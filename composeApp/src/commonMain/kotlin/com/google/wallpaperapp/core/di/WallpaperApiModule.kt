package com.google.wallpaperapp.core.di

import com.google.wallpaperapp.data.remote.PexelWallpapersApi
import com.google.wallpaperapp.data.remote.PexelWallpapersApiImpl
import io.ktor.client.*
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class WallpaperApiModule {

    @Factory
    fun provideWallpaperApi(
        httpClient: HttpClient
    ): PexelWallpapersApi = PexelWallpapersApiImpl(httpClient = httpClient)
}