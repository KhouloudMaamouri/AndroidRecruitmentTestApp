package com.apps.ui.mapper

import com.apps.network.model.NetworkError
import com.apps.ui.R
import com.apps.ui.util.UiText

/**
 * Maps a network-level [NetworkError] to a user-facing [UiText] string resource.
 *
 * @return The corresponding localized [UiText].
 */
fun NetworkError.asUiText(): UiText {
    return when (this) {
        NetworkError.Unauthorized -> UiText.StringResource(R.string.error_unauthorized)
        NetworkError.Forbidden -> UiText.StringResource(R.string.error_forbidden)
        NetworkError.NotFound -> UiText.StringResource(R.string.error_not_found)
        NetworkError.RangeNotSatisfiable -> UiText.StringResource(R.string.error_range_not_satisfiable)
        NetworkError.TooManyRequests -> UiText.StringResource(R.string.error_too_many_requests)
        NetworkError.Server -> UiText.StringResource(R.string.error_server)
        NetworkError.NoInternet -> UiText.StringResource(R.string.error_no_internet)
        NetworkError.Timeout -> UiText.StringResource(R.string.error_timeout)
        NetworkError.Unknown -> UiText.StringResource(R.string.error_unknown)
    }
}