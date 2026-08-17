package com.google.wallpaperapp.domain.mappers

import com.google.wallpaperapp.data.remote.models.WallpaperMainResponse
import com.google.wallpaperapp.domain.models.fullUrl
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * The client is configured with ignoreUnknownKeys, so a dropped field fails silently -- the app
 * just renders a portrait crop on desktop forever. This pins the landscape/original plumbing
 * from raw json all the way through the entity round trip.
 */
class WallpaperMappersTest {

    private val json = Json { isLenient = true; ignoreUnknownKeys = true }

    // Trimmed but otherwise verbatim shape of a Pexels /v1/curated response.
    private val payload = """
        {
          "page": 1, "per_page": 1, "total_results": 8000,
          "next_page": "https://api.pexels.com/v1/curated?page=2",
          "photos": [
            {
              "id": 2014422,
              "width": 3024, "height": 3024,
              "photographer": "Joey Farina",
              "photographer_url": "https://www.pexels.com/@joey",
              "alt": "Green mountain across body of water",
              "src": {
                "original": "https://images.pexels.com/photos/2014422/pexels-photo-2014422.jpeg",
                "large2x": "https://images.pexels.com/photos/2014422/l2x.jpeg",
                "medium": "https://images.pexels.com/photos/2014422/m.jpeg",
                "small": "https://images.pexels.com/photos/2014422/s.jpeg",
                "portrait": "https://images.pexels.com/photos/2014422/p.jpeg",
                "landscape": "https://images.pexels.com/photos/2014422/l.jpeg",
                "tiny": "https://images.pexels.com/photos/2014422/t.jpeg"
              }
            }
          ]
        }
    """.trimIndent()

    @Test
    fun `landscape and original survive json to domain`() {
        val photo = json.decodeFromString<WallpaperMainResponse>(payload).wallpapers.single()
        val wallpaper = photo.toWallpaper()

        assertEquals("https://images.pexels.com/photos/2014422/l.jpeg", wallpaper.landscape)
        assertEquals("https://images.pexels.com/photos/2014422/pexels-photo-2014422.jpeg", wallpaper.original)
        assertEquals("https://images.pexels.com/photos/2014422/p.jpeg", wallpaper.portrait)
    }

    @Test
    fun `landscape and original survive the entity round trip`() {
        val photo = json.decodeFromString<WallpaperMainResponse>(payload).wallpapers.single()
        val roundTripped = photo.toWallpaperEntity().toWallpaper()

        assertEquals(photo.toWallpaper(), roundTripped.copy(alt = photo.toWallpaper().alt))
        assertTrue(roundTripped.landscape.isNotBlank(), "landscape must survive the Room entity")
        assertTrue(roundTripped.original.isNotBlank(), "original must survive the Room entity")
    }

    @Test
    fun `fullUrl prefers original and degrades gracefully`() {
        val wallpaper = json.decodeFromString<WallpaperMainResponse>(payload)
            .wallpapers.single().toWallpaper()

        assertEquals(wallpaper.original, wallpaper.fullUrl)
        // A row cached before schema v3 has neither; it must still resolve to something loadable.
        assertEquals(
            wallpaper.portrait,
            wallpaper.copy(original = "", landscape = "").fullUrl
        )
    }

    @Test
    fun `favouriting keeps portrait as identity and carries landscape alongside`() {
        val wallpaper = json.decodeFromString<WallpaperMainResponse>(payload)
            .wallpapers.single().toWallpaper()
        val favourite = wallpaper.toFavouriteWallpaper()

        // The detail screen matches favourites on the portrait url; that must not drift.
        assertEquals(wallpaper.portrait, favourite.wallpaper)
        assertEquals(wallpaper.landscape, favourite.landscape)
        assertEquals(wallpaper.id, favourite.id)
    }
}
