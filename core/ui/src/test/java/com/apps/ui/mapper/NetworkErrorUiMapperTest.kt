package com.apps.ui.mapper

import com.apps.network.model.NetworkError
import com.apps.ui.R
import com.apps.ui.util.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [NetworkError.asUiText].
 */
class NetworkErrorUiMapperTest {

    @Test
    fun mapsAllNetworkErrorsToExpectedStringResources() {
        val cases = mapOf(
            NetworkError.Unauthorized to R.string.error_unauthorized,
            NetworkError.Forbidden to R.string.error_forbidden,
            NetworkError.NotFound to R.string.error_not_found,
            NetworkError.RangeNotSatisfiable to R.string.error_range_not_satisfiable,
            NetworkError.TooManyRequests to R.string.error_too_many_requests,
            NetworkError.Server to R.string.error_server,
            NetworkError.NoInternet to R.string.error_no_internet,
            NetworkError.Timeout to R.string.error_timeout,
            NetworkError.Unknown to R.string.error_unknown
        )

        for ((error, expectedResId) in cases) {
            val uiText = error.asUiText()
            assertTrue(uiText is UiText.StringResource)
            assertEquals(expectedResId, (uiText as UiText.StringResource).resId)
        }
    }
}
