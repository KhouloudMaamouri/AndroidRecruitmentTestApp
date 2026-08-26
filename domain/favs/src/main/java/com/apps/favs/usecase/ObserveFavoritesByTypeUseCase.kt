package com.apps.favs.usecase


import com.apps.favs.model.FavoriteType
import com.apps.favs.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for observing a stream of favorite item IDs filtered by [FavoriteType].
 *
 * @property repository The [FavoritesRepository] managing favorite state observations.
 */
class ObserveFavoritesByTypeUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    /**
     * Executes observation of favorite IDs for the given type.
     *
     * @param type The [FavoriteType] category to filter by.
     * @return A [Flow] emitting lists of item ID strings.
     */
    operator fun invoke(type: FavoriteType): Flow<List<String>> {
        return repository.observeFavoriteIdsByType(type)
    }
}
