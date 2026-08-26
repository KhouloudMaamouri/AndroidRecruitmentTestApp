package com.apps.favorites.local

import com.apps.favs.model.FavoriteType
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [FavoriteTypeConverter].
 */
class FavoriteTypeConverterTest {

    private val converter = FavoriteTypeConverter()

    @Test
    fun fromFavoriteType_convertsEnumToString() {
        assertEquals("ALBUM", converter.fromFavoriteType(FavoriteType.ALBUM))
    }

    @Test
    fun toFavoriteType_convertsStringToEnum() {
        assertEquals(FavoriteType.ALBUM, converter.toFavoriteType("ALBUM"))
    }

    @Test
    fun toFavoriteType_invalidString_defaultsToAlbum() {
        assertEquals(FavoriteType.ALBUM, converter.toFavoriteType("INVALID_TYPE"))
    }
}
