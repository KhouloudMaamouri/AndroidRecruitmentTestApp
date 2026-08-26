package com.apps.albums

import com.apps.album.model.Album
import com.apps.ui.util.UiText

/**
 * Data class representing the UI state for the Albums screen.
 *
 * @property isLoading Whether the screen is performing initial loading.
 * @property albums List of loaded [Album] items.
 * @property isRefreshing Whether pull-to-refresh operation is active.
 * @property isLoadingMore Whether pagination loading of more items is active.
 * @property canLoadMore Whether more items are available to be loaded from DB.
 * @property error Optional UI error message to display.
 */
data class AlbumsUiState(
    val isLoading: Boolean = false,
    val albums: List<Album> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val error: UiText? = null,
)

/**
 * Sealed interface representing user intents or actions on the Albums screen.
 */
sealed interface AlbumsIntent {
    /** Intent to trigger initial loading of albums. */
    data object Load : AlbumsIntent
    /** Intent to trigger loading more albums when scrolling. */
    data object LoadMore : AlbumsIntent
    /** Intent to retry loading albums after an error. */
    data object Retry : AlbumsIntent
    /**
     * Intent triggered when an album item is clicked.
     *
     * @property album The clicked [Album] item.
     */
    data class AlbumClicked(
        val album: Album,
    ) : AlbumsIntent
    /**
     * Intent triggered to toggle favorite status of an album.
     *
     * @property id Unique identifier of the album item.
     */
    data class ToggleFavorite(
        val id: Int,
    ) : AlbumsIntent
}