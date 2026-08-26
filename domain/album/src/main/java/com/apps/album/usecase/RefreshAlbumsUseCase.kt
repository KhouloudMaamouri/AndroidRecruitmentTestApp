package com.apps.album.usecase

import com.apps.album.error.AlbumError
import com.apps.album.repository.AlbumRepository
import com.apps.common.result.AppResult
import javax.inject.Inject

/**
 * Use case for triggering a refresh of album data from the remote source.
 *
 * @property repository The [AlbumRepository] handling data synchronization.
 */
class RefreshAlbumsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    /**
     * Executes the use case to refresh remote album data.
     *
     * @return An [AppResult] indicating success or failure via [AlbumError].
     */
    suspend operator fun invoke() : AppResult<Unit, AlbumError> {
       return repository.refreshAlbums()
    }
}