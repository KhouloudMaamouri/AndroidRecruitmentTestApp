package com.apps.favs.usecase


import com.apps.favs.model.FavoriteItem
import com.apps.favs.model.FavoriteType
import com.apps.favs.repository.FavoritesRepository
import javax.inject.Inject

/**
 * Use case for toggling the favorite status of an item.
 *
 * @property repository The [FavoritesRepository] managing favorite states.
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    /**
     * Executes the toggle operation for a given [FavoriteItem].
     *
     * @param item The [FavoriteItem] to toggle.
     */
    suspend operator fun invoke(item: FavoriteItem) {
        repository.toggleFavorite(item)
    }

    /**
     * Convenience invocation method to toggle by item ID and [FavoriteType].
     *
     * @param itemId Unique string identifier of the item.
     * @param type The [FavoriteType] category of the item.
     */
    suspend operator fun invoke(itemId: String, type: FavoriteType) {
        invoke(FavoriteItem(itemId = itemId, type = type))
    }
}
