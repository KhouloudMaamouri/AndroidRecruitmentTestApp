package com.apps.album.error

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [AlbumError] domain error sealed interface.
 */
class AlbumErrorTest {

    @Test
    fun albumErrorVariants_instantiateAndCheckTypes() {
        val networkError: AlbumError = AlbumError.NetworkUnavailable
        val serverError: AlbumError = AlbumError.ServerError
        val databaseError: AlbumError = AlbumError.DatabaseError
        val unknownError: AlbumError = AlbumError.Unknown

        assertTrue(networkError is AlbumError.NetworkUnavailable)
        assertTrue(serverError is AlbumError.ServerError)
        assertTrue(databaseError is AlbumError.DatabaseError)
        assertTrue(unknownError is AlbumError.Unknown)

        assertEquals(AlbumError.NetworkUnavailable, networkError)
        assertEquals(AlbumError.ServerError, serverError)
        assertEquals(AlbumError.DatabaseError, databaseError)
        assertEquals(AlbumError.Unknown, unknownError)
    }
}
