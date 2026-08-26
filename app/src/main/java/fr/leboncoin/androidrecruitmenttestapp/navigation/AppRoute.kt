package fr.leboncoin.androidrecruitmenttestapp.navigation

import kotlinx.serialization.Serializable

/**
 * Sealed interface representing type-safe navigation destinations within the app.
 */
@Serializable
sealed interface AppRoute {

    /**
     * Navigation destination for the list of albums screen.
     */
    @Serializable
    data object Albums : AppRoute

    /**
     * Navigation destination for the album detail screen.
     *
     * @property albumId The unique identifier of the selected album.
     */
    @Serializable
    data class AlbumDetails(
        val albumId: Int
    ) : AppRoute
}