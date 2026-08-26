package com.apps.album.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [Album] domain model.
 */
class AlbumTest {

    @Test
    fun album_defaultIsFavorite_isFalse() {
        val album = Album(
            id = 1,
            albumId = 10,
            title = "Sample Title",
            url = "https://example.com/image.jpg",
            thumbnailUrl = "https://example.com/thumb.jpg"
        )

        assertEquals(1, album.id)
        assertEquals(10, album.albumId)
        assertEquals("Sample Title", album.title)
        assertEquals("https://example.com/image.jpg", album.url)
        assertEquals("https://example.com/thumb.jpg", album.thumbnailUrl)
        assertFalse(album.isFavorite)
    }

    @Test
    fun album_copyWithIsFavoriteTrue_updatesFavoriteState() {
        val album = Album(
            id = 2,
            albumId = 20,
            title = "Test Title",
            url = "https://example.com/2.jpg",
            thumbnailUrl = "https://example.com/2_thumb.jpg",
            isFavorite = false
        )

        val updatedAlbum = album.copy(isFavorite = true)

        assertTrue(updatedAlbum.isFavorite)
        assertEquals(album.id, updatedAlbum.id)
        assertEquals(album.title, updatedAlbum.title)
    }
}
