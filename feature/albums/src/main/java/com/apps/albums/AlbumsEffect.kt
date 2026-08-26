package com.apps.albums

/**
 * Sealed interface representing one-off side effects emitted by the Albums ViewModel.
 */
sealed interface AlbumsEffect {
    /**
     * Side effect to navigate to the detailed view of a specific album.
     *
     * @property albumId Unique identifier of the album to navigate to.
     */
    data class NavigateToAlbum(
        val albumId: Int,
    ) : AlbumsEffect

    /**
     * Side effect to present a transient error message to the user.
     *
     * @property message Error message content.
     */
    data class ShowError(
        val message: String,
    ) : AlbumsEffect
}