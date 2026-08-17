package com.google.wallpaperapp.data.remote

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

/**
 * Retries cover transient failures (429, 5xx) and must not cover 401: a rejected key is rejected
 * every time, so retrying it would only delay the error the user needs to see.
 */
class RetryRecoversTest {

    private val jsonHeaders =
        headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

    private val okBody = """{"page":1,"per_page":1,"total_results":10,"photos":[
        {"id":1,"photographer":"p","photographer_url":"u","alt":"a",
         "src":{"medium":"m","portrait":"p","small":"s","landscape":"l","original":"o"}}]}"""

    private val rejected = """{"status":401,"code":"Unauthorized","message":"Invalid API key"}"""

    /** Fails the first [failures] attempts, then succeeds. */
    private fun apiFailingFirst(failures: Int, status: HttpStatusCode): Pair<PexelWallpapersApi, () -> Int> {
        var attempts = 0
        val engine = MockEngine {
            attempts++
            if (attempts <= failures) {
                respond(content = rejected, status = status, headers = jsonHeaders)
            } else {
                respond(content = okBody, status = HttpStatusCode.OK, headers = jsonHeaders)
            }
        }
        return PexelWallpapersApiImpl(NetworkModule().provideHttpClient(engine)) to { attempts }
    }

    @Test
    fun `a transient 5xx is retried and the page loads`() = runBlocking {
        val (api, attempts) = apiFailingFirst(failures = 2, status = HttpStatusCode.BadGateway)

        val response = api.searchWallpaper(page = 4, query = "flowers", perPage = 1)

        assertEquals(10, response.totalResults, "the request should have recovered after retries")
        assertEquals(3, attempts(), "should have taken two retries before succeeding")
    }

    @Test
    fun `a 401 is not retried -- a rejected key fails the same way every time`() = runBlocking {
        var attempts = 0
        val engine = MockEngine {
            attempts++
            respond(content = rejected, status = HttpStatusCode.Unauthorized, headers = jsonHeaders)
        }
        val api = PexelWallpapersApiImpl(NetworkModule().provideHttpClient(engine))

        runCatching { api.getWallpapers(page = 1) }

        assertEquals(1, attempts, "401 must fail fast rather than burning retries")
    }

    @Test
    fun `a 429 is retried too`() = runBlocking {
        val (api, attempts) = apiFailingFirst(failures = 1, status = HttpStatusCode.TooManyRequests)

        api.getWallpapers(page = 1, perPage = 1)

        assertEquals(2, attempts())
    }

    @Test
    fun `a persistent 5xx gives up rather than retrying forever`() = runBlocking {
        var attempts = 0
        val engine = MockEngine {
            attempts++
            respond(content = rejected, status = HttpStatusCode.BadGateway, headers = jsonHeaders)
        }
        val api = PexelWallpapersApiImpl(NetworkModule().provideHttpClient(engine))

        val thrown = runCatching { api.getWallpapers(page = 1) }.exceptionOrNull()

        assertTrue(thrown != null, "a persistent server error must still surface")
        assertTrue(attempts <= 4, "bounded retries: 1 attempt + 3 retries, was $attempts")
    }
}
