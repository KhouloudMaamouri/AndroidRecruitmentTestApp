package com.apps.albums.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Room-backed implementation of [AlbumLocalDataSource].
 *
 * @property dao The [AlbumDao] used to interact with Room database tables.
 */
class AlbumLocalDataSourceImpl @Inject constructor(
    private val dao: AlbumDao
) : AlbumLocalDataSource {

    /**
     * Observes cached album entities from the database with pagination support.
     *
     * @param limit Maximum number of items to return, defaults to 10.
     * @param offset Number of items to skip, defaults to 0.
     * @return A [Flow] emitting lists of [AlbumEntity].
     */
    override fun observeAlbums(
        limit: Int,
        offset: Int
    ): Flow<List<AlbumEntity>> {
        return dao.observeAlbums(limit = limit, offset = offset)
    }

    /**
     * Observes a single album entity by ID.
     *
     * @param id The album item ID.
     * @return A [Flow] emitting the [AlbumEntity] or null.
     */
    override fun observeAlbum(id: Int): Flow<AlbumEntity?> {
        return dao.observeAlbum(id)
    }

    /**
     * Replaces cached albums in the database with fresh data.
     *
     * @param albums List of [AlbumEntity] records.
     */
    override suspend fun saveAlbums(
        albums: List<AlbumEntity>
    ) {
        dao.replaceAlbums(albums)
    }
}