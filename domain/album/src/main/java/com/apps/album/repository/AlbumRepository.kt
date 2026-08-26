package com.apps.album.repository

import com.apps.album.model.Album
import com.apps.album.error.AlbumError
import com.apps.common.result.AppResult
import kotlinx.coroutines.flow.Flow


/**
 * Interface defining data operations and observations for album entries.
 */
interface AlbumRepository {

    /**
     * Observes the list of cached albums with pagination support.
     *
     * @param limit Maximum number of items to return, default is 10.
     * @param offset Number of items to skip, default is 0.
     * @return A [Flow] emitting an [AppResult] containing a list of [Album]s or an [AlbumError].
     */
    fun observeAlbums(
        limit: Int = 10,
        offset: Int = 0
    ): Flow<AppResult<List<Album>, AlbumError>>

    /**
     * Observes a specific album by its unique identifier.
     *
     * @param id The unique identifier of the album item.
     * @return A [Flow] emitting an [AppResult] containing the [Album] (or null) or an [AlbumError].
     */
    fun observeAlbum(id: Int): Flow<AppResult<Album?, AlbumError>>

    /**
     * Triggers a remote refresh of album data and updates the local cache.
     *
     * @return An [AppResult] indicating success or failure via [AlbumError].
     */
    suspend fun refreshAlbums() : AppResult<Unit, AlbumError>

    /**
     * Toggles the favorite status of the specified album item.
     *
     * @param id The unique identifier of the album item.
     */
    suspend fun toggleFavorite(id: Int)

    /**
     * Observes the favorite status of a specific album item.
     *
     * @param id The unique identifier of the album item.
     * @return A [Flow] emitting true if marked as favorite, false otherwise.
     */
    fun observeFavorite(id: Int): Flow<Boolean>
}