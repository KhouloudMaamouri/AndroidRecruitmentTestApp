package com.apps.albums.mapper

import android.database.sqlite.SQLiteException
import com.apps.album.error.AlbumError
import com.apps.albums.local.AlbumEntity
import com.apps.albums.remote.AlbumDto
import com.apps.network.model.NetworkError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [AlbumMapper].
 */
class AlbumMapperTest {

    @Test
    fun dtoToEntity_mapsCorrectly() {
        val dto = AlbumDto(
            id = 1,
            albumId = 10,
            title = "Test Dto",
            url = "https://example.com/img.png",
            thumbnailUrl = "https://example.com/thumb.png"
        )

        val entity = dto.toEntity()

        assertEquals(1, entity.id)
        assertEquals(10, entity.albumId)
        assertEquals("Test Dto", entity.title)
        assertEquals("https://example.com/img.png", entity.url)
        assertEquals("https://example.com/thumb.png", entity.thumbnailUrl)
    }

    @Test
    fun entityToDomain_mapsCorrectlyWithFavoriteFlag() {
        val entity = AlbumEntity(
            id = 2,
            albumId = 20,
            title = "Test Entity",
            url = "https://example.com/img2.png",
            thumbnailUrl = "https://example.com/thumb2.png"
        )

        val domainFalse = entity.toDomain(isFavorite = false)
        assertFalse(domainFalse.isFavorite)
        assertEquals(2, domainFalse.id)
        assertEquals("Test Entity", domainFalse.title)

        val domainTrue = entity.toDomain(isFavorite = true)
        assertTrue(domainTrue.isFavorite)
    }

    @Test
    fun networkErrorToAlbumError_mapsCorrectly() {
        assertEquals(AlbumError.NetworkUnavailable, NetworkError.NoInternet.toAlbumError())
        assertEquals(AlbumError.NetworkUnavailable, NetworkError.Timeout.toAlbumError())
        assertEquals(AlbumError.ServerError, NetworkError.Server.toAlbumError())
        assertEquals(AlbumError.Unknown, NetworkError.Unknown.toAlbumError())
        assertEquals(AlbumError.Unknown, NetworkError.Unauthorized.toAlbumError())
    }

    @Test
    fun dbErrorToAlbumError_mapsCorrectly() {
        val sqliteError = SQLiteException("Database locked")
        assertEquals(AlbumError.DatabaseError, sqliteError.toAlbumDBError())

        val genericError = IllegalArgumentException("Bad arg")
        assertEquals(AlbumError.Unknown, genericError.toAlbumDBError())
    }
}
