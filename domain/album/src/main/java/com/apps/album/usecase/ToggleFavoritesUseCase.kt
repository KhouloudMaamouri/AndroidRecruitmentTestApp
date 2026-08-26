package com.apps.album.usecase

import com.apps.album.repository.AlbumRepository
import javax.inject.Inject

/**
 * Use case for toggling the favorite status of a specific album item.
 *
 * @property repository The [AlbumRepository] managing favorite state updates.
 */
class ToggleFavoritesUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    /**
     * Executes the use case to toggle favorite status.
     *
     * @param id The unique identifier of the album item.
     */
    suspend operator fun invoke(id: Int) {
        repository.toggleFavorite(id)
    }
}