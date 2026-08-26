package com.apps.album.usecase

import com.apps.album.error.AlbumError
import com.apps.album.model.Album
import com.apps.album.repository.AlbumRepository
import com.apps.common.result.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for observing a single album by its identifier.
 *
 * @property repository The [AlbumRepository] source for album data.
 */
class ObserveAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    /**
     * Executes the use case to observe a specific album.
     *
     * @param id Unique identifier of the target album item.
     * @return A [Flow] emitting an [AppResult] containing the [Album] (or null) or an [AlbumError].
     */
    operator fun invoke(id: Int): Flow<AppResult<Album?, AlbumError>> {
        return repository.observeAlbum(id)
    }
}
