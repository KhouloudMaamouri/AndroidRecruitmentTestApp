package com.apps.ui.mapper

import com.apps.album.error.AlbumError
import com.apps.ui.R
import com.apps.ui.util.UiText

/**
 * Maps a domain-level [AlbumError] to a user-facing [UiText] string resource.
 *
 * @return The corresponding localized [UiText].
 */
fun AlbumError.asUiText(): UiText {
    return when (this) {
        AlbumError.NetworkUnavailable ->
            UiText.StringResource(R.string.error_no_internet)

        AlbumError.ServerError ->
            UiText.StringResource(R.string.error_server)

        AlbumError.DatabaseError ->
            UiText.StringResource(R.string.error_database)

        AlbumError.Unknown ->
            UiText.StringResource(R.string.error_unknown)
    }
}