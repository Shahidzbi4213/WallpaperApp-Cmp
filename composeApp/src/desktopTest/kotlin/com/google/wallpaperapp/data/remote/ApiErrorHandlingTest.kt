package com.google.wallpaperapp.data.remote

import com.google.wallpaperapp.data.utils.toUserMessage
import com.google.wallpaperapp.di.NetworkModule
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.error_unauthorized

/**
 * Regression for the reported bug: a Pexels 401 body was being fed to the WallpaperMainResponse
 * deserializer, so the UI showed "Fields [page, per_page, photos, total_results] are required"
 * instead of saying the request had been rejected.
 *
 * Uses runBlocking rather than runTest: runTest's virtual clock advances instantly, which trips
 * the client's 10s HttpTimeout before the mock engine ever answers.
 */
class ApiErrorHandlingTest {

    private val jsonHeaders =
        headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

    private fun apiReturning(status: HttpStatusCode, body: String): PexelWallpapersApi {
        val engine = MockEngine { respond(content = body, status = status, headers = jsonHeaders) }
        return PexelWallpapersApiImpl(NetworkModule().provideHttpClient(engine))
    }

    @Test
    fun `a 401 surfaces as a rejected-key message, not a serialization complaint`() = runBlocking {
        val api = apiReturning(
            HttpStatusCode.Unauthorized,
            """{"status":401,"code":"Unauthorized","message":"Invalid API key"}"""
        )

        val thrown = runCatching { api.getWallpapers(page = 1) }.exceptionOrNull()
        assertTrue(thrown != null, "a 401 must not be treated as a successful body")
        assertTrue(
            thrown!!::class.simpleName?.contains("JsonConvert") != true,
            "the failure must carry the HTTP status, not a deserialization error: $thrown"
        )
        assertEquals(Res.string.error_unauthorized, thrown.toUserMessage())
    }

    @Test
    fun `a valid 200 body still parses`() = runBlocking {
        val api = apiReturning(
            HttpStatusCode.OK,
            """{"page":1,"per_page":1,"total_results":10,"photos":[
                 {"id":1,"photographer":"p","photographer_url":"u","alt":"a",
                  "src":{"medium":"m","portrait":"p","small":"s","landscape":"l","original":"o"}}]}"""
        )

        val response = api.getWallpapers(page = 1, perPage = 1)
        assertEquals(10, response.totalResults)
        assertEquals("l", response.wallpapers.single().wallpaperSource.landscape)
    }
}
