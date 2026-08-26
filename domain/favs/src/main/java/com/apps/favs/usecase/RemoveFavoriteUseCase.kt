package com.apps.favs.usecase

import com.apps.favs.model.FavoriteItem
import com.apps.favs.model.FavoriteType
import com.apps.favs.repository.FavoritesRepository
import javax.inject.Inject

/**
 * Use case for removing an item from favorites.
 *
 * @property repository The [FavoritesRepository] for persistent storage operations.
 */
class RemoveFavoriteUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    /**
     * Executes removing the specified [FavoriteItem].
     *
     * @param item The [FavoriteItem] to remove.
     */
    suspend operator fun invoke(item: FavoriteItem) {
        repository.removeFavorite(item)
    }

    /**
     * Convenience invocation method to remove by item ID and [FavoriteType].
     *
     * @param itemId Unique string identifier of the item.
     * @param type The [FavoriteType] category of the item.
     */
    suspend operator fun invoke(itemId: String, type: FavoriteType) {
        invoke(FavoriteItem(itemId = itemId, type = type))
    }
}
