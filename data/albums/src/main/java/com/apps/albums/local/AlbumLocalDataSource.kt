package com.apps.albums.local

import kotlinx.coroutines.flow.Flow

/**
 * Interface defining local database storage operations for cached albums.
 */
interface AlbumLocalDataSource {

    /**
     * Observes cached album entities with pagination support.
     *
     * @param limit Maximum number of items to return, defaults to 10.
     * @param offset Number of items to skip, defaults to 0.
     * @return A [Flow] emitting lists of [AlbumEntity].
     */
    fun observeAlbums(
        limit: Int = 10,
        offset: Int = 0
    ): Flow<List<AlbumEntity>>

    /**
     * Observes a single cached album entity by its ID.
     *
     * @param id The unique identifier of the album item.
     * @return A [Flow] emitting the [AlbumEntity] or null.
     */
    fun observeAlbum(id: Int): Flow<AlbumEntity?>

    /**
     * Replaces the current cached albums with a new list of album entities.
     *
     * @param albums List of [AlbumEntity] to save.
     */
    suspend fun saveAlbums(albums: List<AlbumEntity>)
}