package com.apps.ui.mapper

import com.apps.album.error.AlbumError
import com.apps.ui.R
import com.apps.ui.util.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [AlbumError.asUiText].
 */
class AlbumErrorUiMapperTest {

    @Test
    fun mapsAllAlbumErrorsToExpectedStringResources() {
        val cases = mapOf(
            AlbumError.NetworkUnavailable to R.string.error_no_internet,
            AlbumError.ServerError to R.string.error_server,
            AlbumError.DatabaseError to R.string.error_database,
            AlbumError.Unknown to R.string.error_unknown
        )

        for ((error, expectedResId) in cases) {
            val uiText = error.asUiText()
            assertTrue(uiText is UiText.StringResource)
            assertEquals(expectedResId, (uiText as UiText.StringResource).resId)
        }
    }
}
