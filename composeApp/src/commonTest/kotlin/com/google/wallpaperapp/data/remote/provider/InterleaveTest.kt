package com.google.wallpaperapp.data.remote.provider

import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.domain.models.WallpaperSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InterleaveTest {

    private fun wallpaper(source: WallpaperSource, n: Int) = Wallpaper(
        id = source.idOf(n.toString()),
        source = source,
        thumbUrl = "thumb/$n",
        previewUrl = "preview/$n",
        fullUrl = "full/$n",
    )

    @Test
    fun `alternates between providers of equal length`() {
        val a = List(3) { wallpaper(WallpaperSource.WALLHAVEN, it) }
        val b = List(3) { wallpaper(WallpaperSource.PEXELS, it) }

        val merged = interleave(listOf(a, b))

        assertEquals(6, merged.size)
        assertEquals(
            listOf(
                WallpaperSource.WALLHAVEN, WallpaperSource.PEXELS,
                WallpaperSource.WALLHAVEN, WallpaperSource.PEXELS,
                WallpaperSource.WALLHAVEN, WallpaperSource.PEXELS,
            ),
            merged.map { it.source },
        )
    }

    @Test
    fun `keeps every item when providers return different page sizes`() {
        val long = List(5) { wallpaper(WallpaperSource.WALLHAVEN, it) }
        val short = List(2) { wallpaper(WallpaperSource.BING, it) }

        val merged = interleave(listOf(long, short))

        assertEquals(7, merged.size)
        // The shorter provider runs out and the deeper one carries the tail.
        assertEquals(
            List(3) { WallpaperSource.WALLHAVEN },
            merged.takeLast(3).map { it.source },
        )
    }

    @Test
    fun `drops empty lists without disturbing order`() {
        val a = List(2) { wallpaper(WallpaperSource.PEXELS, it) }

        val merged = interleave(listOf(emptyList(), a, emptyList()))

        assertEquals(a, merged)
    }

    @Test
    fun `returns empty for no input`() {
        assertTrue(interleave(emptyList()).isEmpty())
        assertTrue(interleave(listOf(emptyList(), emptyList())).isEmpty())
    }

    @Test
    fun `de-duplicates ids appearing in more than one list`() {
        val shared = wallpaper(WallpaperSource.PEXELS, 1)

        val merged = interleave(listOf(listOf(shared), listOf(shared)))

        assertEquals(1, merged.size)
    }
}
