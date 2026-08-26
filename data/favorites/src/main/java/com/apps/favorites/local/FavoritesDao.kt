package com.apps.favorites.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object (DAO) providing database operations for user favorites.
 */
@Dao
interface FavoritesDao {

    /**
     * Inserts or replaces a favorite record in the database.
     *
     * @param favorite The [FavoriteEntity] record to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    /**
     * Deletes a favorite record matching the specified item ID and category type.
     *
     * @param itemId Unique identifier of the item.
     * @param type String representation of the favorite category type.
     */
    @Query("DELETE FROM favorites WHERE itemId = :itemId AND type = :type")
    suspend fun deleteFavorite(itemId: String, type: String)

    /**
     * Observes the list of favorite item IDs for a given category type.
     *
     * @param type String representation of the favorite category type.
     * @return A [Flow] emitting a list of favorite item ID strings.
     */
    @Query("SELECT itemId FROM favorites WHERE type = :type")
    fun observeFavoriteIdsByType(type: String): Flow<List<String>>

    /**
     * Observes whether a specific item ID of a given category type is in favorites.
     *
     * @param itemId Unique identifier of the item.
     * @param type String representation of the favorite category type.
     * @return A [Flow] emitting true if favorite, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = :itemId AND type = :type)")
    fun observeIsFavorite(itemId: String, type: String): Flow<Boolean>

    /**
     * Synchronously/suspendingly checks if a specific item is in favorites.
     *
     * @param itemId Unique identifier of the item.
     * @param type String representation of the favorite category type.
     * @return True if favorite, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = :itemId AND type = :type)")
    suspend fun isFavorite(itemId: String, type: String): Boolean
}
