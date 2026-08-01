package com.google.wallpaperapp.data.remote.provider

import com.google.wallpaperapp.domain.models.WallpaperSource
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Parses a trimmed sample of each provider's real response.
 *
 * These run offline and exist to catch wire-format drift and mapping mistakes —
 * particularly that every provider produces a namespaced id and a usable
 * full-resolution URL.
 */
class ProviderDtoTest {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    @Test
    fun `parses a pexels page and maps it`() {
        val body = """
        {
          "page": 1,
          "per_page": 1,
          "next_page": "https://api.pexels.com/v1/curated?page=2",
          "photos": [
            {
              "id": 2014422,
              "width": 3024,
              "height": 3024,
              "url": "https://www.pexels.com/photo/brown-rocks-2014422/",
              "photographer": "Joey Farina",
              "photographer_url": "https://www.pexels.com/@joey",
              "alt": "Brown Rocks During Golden Hour",
              "src": {
                "original": "https://images.pexels.com/photos/2014422/pexels-photo.jpeg",
                "large": "https://images.pexels.com/photos/2014422/large.jpeg",
                "medium": "https://images.pexels.com/photos/2014422/medium.jpeg",
                "portrait": "https://images.pexels.com/photos/2014422/portrait.jpeg",
                "small": "https://images.pexels.com/photos/2014422/small.jpeg"
              }
            }
          ]
        }
        """.trimIndent()

        val page = json.decodeFromString<PexelsListDto>(body).toProviderPage()
        val wallpaper = page.wallpapers.single()

        assertTrue(page.hasMore)
        assertEquals("pexels:2014422", wallpaper.id)
        assertEquals(WallpaperSource.PEXELS, wallpaper.source)
        // The original, not the portrait derivative the app used to download.
        assertEquals("https://images.pexels.com/photos/2014422/pexels-photo.jpeg", wallpaper.fullUrl)
        assertEquals("https://images.pexels.com/photos/2014422/portrait.jpeg", wallpaper.previewUrl)
        assertEquals("Joey Farina", wallpaper.authorName)
        assertEquals(3024, wallpaper.width)
    }

    @Test
    fun `last pexels page reports no more results`() {
        val body = """{"page":9,"per_page":24,"photos":[]}"""
        val page = json.decodeFromString<PexelsListDto>(body).toProviderPage()

        assertFalse(page.hasMore)
        assertTrue(page.wallpapers.isEmpty())
    }

    @Test
    fun `parses a wallhaven page and maps it`() {
        val body = """
        {
          "data": [
            {
              "id": "94x38z",
              "url": "https://wallhaven.cc/w/94x38z",
              "purity": "sfw",
              "category": "general",
              "dimension_x": 1440,
              "dimension_y": 3200,
              "resolution": "1440x3200",
              "ratio": "0.45",
              "file_size": 5070446,
              "file_type": "image/jpeg",
              "colors": ["#000000", "#abbcda"],
              "path": "https://w.wallhaven.cc/full/94/wallhaven-94x38z.jpg",
              "thumbs": {
                "large": "https://th.wallhaven.cc/lg/94/94x38z.jpg",
                "original": "https://th.wallhaven.cc/orig/94/94x38z.jpg",
                "small": "https://th.wallhaven.cc/small/94/94x38z.jpg"
              }
            }
          ],
          "meta": { "current_page": 1, "last_page": 36, "per_page": 24, "total": 848 }
        }
        """.trimIndent()

        val page = json.decodeFromString<WallhavenListDto>(body).toProviderPage("mountains")
        val wallpaper = page.wallpapers.single()

        assertTrue(page.hasMore)
        assertEquals("wallhaven:94x38z", wallpaper.id)
        assertEquals("https://w.wallhaven.cc/full/94/wallhaven-94x38z.jpg", wallpaper.fullUrl)
        assertEquals(3200, wallpaper.height)
        // Search listings carry no tags, so the query stands in as alt text.
        assertEquals("mountains", wallpaper.alt)
    }

    @Test
    fun `wallhaven reports no more results on the last page`() {
        val body = """{"data":[],"meta":{"current_page":36,"last_page":36}}"""
        val page = json.decodeFromString<WallhavenListDto>(body).toProviderPage(null)

        assertFalse(page.hasMore)
    }

    @Test
    fun `parses a bing response and builds the UHD url`() {
        val body = """
        {
          "images": [
            {
              "startdate": "20260801",
              "url": "/th?id=OHR.LakeBled_EN-US1234567890_1920x1080.jpg",
              "urlbase": "/th?id=OHR.LakeBled_EN-US1234567890",
              "copyright": "Lake Bled, Slovenia (© Jane Doe/Getty)",
              "copyrightlink": "https://www.bing.com/search?q=Lake+Bled",
              "title": "Lake Bled",
              "hsh": "abc123"
            }
          ]
        }
        """.trimIndent()

        val page = json.decodeFromString<BingListDto>(body).toProviderPage()
        val wallpaper = page.wallpapers.single()

        assertEquals("bing:abc123", wallpaper.id)
        assertEquals(
            "https://www.bing.com/th?id=OHR.LakeBled_EN-US1234567890_UHD.jpg",
            wallpaper.fullUrl,
        )
        assertEquals("Lake Bled", wallpaper.alt)
        // Bing never paginates.
        assertFalse(page.hasMore)
    }

    @Test
    fun `every provider namespaces its ids so they cannot collide`() {
        val pexels = json.decodeFromString<PexelsListDto>(
            """{"photos":[{"id":1,"src":{"original":"o"}}]}"""
        ).toProviderPage().wallpapers.single()
        val wallhaven = json.decodeFromString<WallhavenListDto>(
            """{"data":[{"id":"1","path":"p"}],"meta":{"current_page":1,"last_page":1}}"""
        ).toProviderPage(null).wallpapers.single()

        // Same underlying id "1", different namespaced ids.
        assertEquals("pexels:1", pexels.id)
        assertEquals("wallhaven:1", wallhaven.id)
    }
}
