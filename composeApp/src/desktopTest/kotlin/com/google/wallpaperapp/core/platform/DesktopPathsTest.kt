package com.google.wallpaperapp.core.platform

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class DesktopPathsTest {

    @Test
    fun `currentOs matches the running platform`() {
        val name = System.getProperty("os.name").lowercase()
        val expected = when {
            name.contains("mac") -> DesktopOs.MAC
            name.contains("win") -> DesktopOs.WINDOWS
            name.contains("nux") -> DesktopOs.LINUX
            else -> currentOs
        }
        assertEquals(expected, currentOs)
        assertNotEquals(DesktopOs.UNKNOWN, currentOs, "os.name '$name' was not recognised")
    }

    @Test
    fun `app data and download dirs are absolute and usable`() {
        for (dir in listOf(appDataDir(), downloadsDir())) {
            assertTrue(dir.isAbsolute, "$dir should be absolute")
            assertTrue(dir.exists(), "$dir should have been created on access")
            assertTrue(dir.isDirectory, "$dir should be a directory")
        }
    }

    @Test
    fun `download filenames are stripped of query strings and unsafe characters`() {
        assertEquals(
            "pexels-photo-1234.jpeg",
            "https://images.pexels.com/photos/1234/pexels-photo-1234.jpeg?auto=compress&w=800"
                .sanitizedFileName()
        )
        // No extension in the url -> we still produce an openable file.
        assertEquals("photo.jpg", "https://example.com/a b/photo".sanitizedFileName())
        assertEquals("wallpaper.jpg", "".sanitizedFileName())
    }
}
