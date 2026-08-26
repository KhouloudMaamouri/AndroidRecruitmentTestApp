package com.apps.album.usecase

import com.apps.album.error.AlbumError
import com.apps.album.model.Album
import com.apps.album.repository.AlbumRepository
import com.apps.common.result.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * Use case for observing the stream of all albums.
 *
 * @property repository The [AlbumRepository] source for album data.
 */
class ObserveAlbumsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    /**
     * Executes the use case to observe albums with pagination support.
     *
     * @param limit Maximum number of items to return, default is 10.
     * @param offset Number of items to skip, default is 0.
     * @return A [Flow] emitting an [AppResult] containing a list of [Album]s or an [AlbumError].
     */
    operator fun invoke(
        limit: Int = 10,
        offset: Int = 0
    ): Flow<AppResult<List<Album>, AlbumError>> {
        return repository.observeAlbums(limit = limit, offset = offset)
    }
}