package com.apps.albums.remote

import retrofit2.http.GET

/**
 * Retrofit API interface for fetching album data endpoints.
 */
interface AlbumApi {

    /**
     * Fetches the full list of album Data Transfer Objects from the remote server.
     *
     * @return A list of [AlbumDto] items.
     */
    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): List<AlbumDto>
}