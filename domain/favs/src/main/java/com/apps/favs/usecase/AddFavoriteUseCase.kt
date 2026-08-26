package com.apps.favs.usecase

import com.apps.favs.model.FavoriteItem
import com.apps.favs.model.FavoriteType
import com.apps.favs.repository.FavoritesRepository
import javax.inject.Inject

/**
 * Use case for adding an item to favorites.
 *
 * @property repository The [FavoritesRepository] for persistent storage operations.
 */
class AddFavoriteUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    /**
     * Executes adding the specified [FavoriteItem].
     *
     * @param item The [FavoriteItem] to add.
     */
    suspend operator fun invoke(item: FavoriteItem) {
        repository.addFavorite(item)
    }

    /**
     * Convenience invocation method to add by item ID and [FavoriteType].
     *
     * @param itemId Unique string identifier of the item.
     * @param type The [FavoriteType] category of the item.
     */
    suspend operator fun invoke(itemId: String, type: FavoriteType) {
        invoke(FavoriteItem(itemId = itemId, type = type))
    }
}
