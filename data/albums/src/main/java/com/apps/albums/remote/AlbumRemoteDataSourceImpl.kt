package com.apps.albums.remote

import com.apps.util.NetworkResult
import com.apps.util.safeApiCall
import javax.inject.Inject

/**
 * Implementation of [AlbumRemoteDataSource] executing network requests via [AlbumApi].
 *
 * @property api The [AlbumApi] Retrofit service.
 */
class AlbumRemoteDataSourceImpl @Inject constructor(
    private val api: AlbumApi
) : AlbumRemoteDataSource {

    /**
     * Executes safe API call to retrieve album list from remote network endpoint.
     *
     * @return A [NetworkResult] wrapping the API response.
     */
    override suspend fun getAlbums(): NetworkResult<List<AlbumDto>> {
        return safeApiCall { api.getAlbums() }
    }
}