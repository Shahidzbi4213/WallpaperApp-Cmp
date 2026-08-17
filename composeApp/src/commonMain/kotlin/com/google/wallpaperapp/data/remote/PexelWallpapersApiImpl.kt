package com.google.wallpaperapp.data.remote

import com.google.wallpaperapp.data.remote.models.WallpaperMainResponse
import com.google.wallpaperapp.data.utils.HttpRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter


class PexelWallpapersApiImpl(private val httpClient: HttpClient) : PexelWallpapersApi {

    override suspend fun getWallpapers(page: Int, perPage: Int): WallpaperMainResponse =
        httpClient.get(HttpRoutes.WALLPAPERS) {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun searchWallpaper(
        page: Int,
        query: String,
        perPage: Int
    ): WallpaperMainResponse =
        httpClient.get(HttpRoutes.SEARCH_WALLPAPERS) {
            parameter("query", query)
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}
