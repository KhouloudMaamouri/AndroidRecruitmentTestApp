package com.apps.favs.repository

import com.apps.favs.model.FavoriteItem
import com.apps.favs.model.FavoriteType
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository interface managing operations for favorite items.
 */
interface FavoritesRepository {

    /**
     * Adds an item to user favorites.
     *
     * @param item The [FavoriteItem] to add.
     */
    suspend fun addFavorite(item: FavoriteItem)

    /**
     * Removes an item from user favorites.
     *
     * @param item The [FavoriteItem] to remove.
     */
    suspend fun removeFavorite(item: FavoriteItem)

    /**
     * Observes the list of favorite item IDs for a given [FavoriteType].
     *
     * @param type The [FavoriteType] to filter by.
     * @return A [Flow] emitting a list of favorite item IDs.
     */
    fun observeFavoriteIdsByType(type: FavoriteType): Flow<List<String>>

    /**
     * Checks whether a specific item is currently favorite.
     *
     * @param itemId Unique item identifier.
     * @param type The [FavoriteType] of the item.
     * @return True if favorite, false otherwise.
     */
    suspend fun isFavorite(itemId: String, type: FavoriteType): Boolean

    /**
     * Observes whether a specific item is favorite.
     *
     * @param itemId Unique item identifier.
     * @param type The [FavoriteType] of the item.
     * @return A [Flow] emitting true if favorite, false otherwise.
     */
    fun observeIsFavorite(itemId: String, type: FavoriteType): Flow<Boolean>

    /**
     * Toggles the favorite status for the specified item.
     *
     * @param item The [FavoriteItem] to toggle.
     */
    suspend fun toggleFavorite(item: FavoriteItem)
}
