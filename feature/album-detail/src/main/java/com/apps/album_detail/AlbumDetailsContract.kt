package com.apps.album_detail

import com.apps.album.model.Album
import com.apps.ui.util.UiText

/**
 * UI state for the Album Details screen.
 *
 * @property isLoading Whether album details are currently being loaded.
 * @property album The loaded [Album] object, or null if not yet loaded.
 * @property isRefreshing Whether a background refresh is taking place.
 * @property error Optional UI error message.
 */
data class AlbumDetailUiState (
    val isLoading: Boolean = false,
    val album: Album? = null,
    val isRefreshing: Boolean = false,
    val error: UiText? = null,
)

/**
 * Sealed interface representing user intents for the Album Details screen.
 */
sealed interface AlbumDetailIntent {
    /**
     * Intent to load a specific album by its ID.
     *
     * @property albumId Target album identifier.
     */
    data class Load(val albumId: Int) : AlbumDetailIntent

    /** Intent to retry loading album details after an error. */
    data object Retry : AlbumDetailIntent

    /** Intent to toggle the favorite status of the displayed album. */
    data object ToggleFavorite : AlbumDetailIntent
}