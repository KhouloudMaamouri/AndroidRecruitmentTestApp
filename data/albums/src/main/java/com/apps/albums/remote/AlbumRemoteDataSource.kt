package com.apps.albums.remote

import com.apps.util.NetworkResult

/**
 * Interface defining network fetching operations for album remote data.
 */
interface AlbumRemoteDataSource {

    /**
     * Performs a network call to fetch all album entries.
     *
     * @return A [NetworkResult] containing a list of [AlbumDto].
     */
    suspend fun getAlbums(): NetworkResult<List<AlbumDto>>
}