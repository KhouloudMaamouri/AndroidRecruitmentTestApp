package com.apps.favorites.repository

import com.apps.favorites.local.FavoriteEntity
import com.apps.favorites.local.FavoritesDao
import com.apps.favs.model.FavoriteItem
import com.apps.favs.model.FavoriteType
import com.apps.favs.repository.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Production-ready implementation of [FavoritesRepository] backed by Room database via [FavoritesDao].
 * Handles mapping between domain models and Room database entities.
 *
 * @property dao Room DAO interface for executing database operations.
 */
class FavoritesRepositoryImpl @Inject constructor(
    private val dao: FavoritesDao,
) : FavoritesRepository {

    override suspend fun addFavorite(item: FavoriteItem) =
        withContext(Dispatchers.IO) {
            val entity = FavoriteEntity(
                itemId = item.itemId,
                type = item.type.name
            )
            dao.insertFavorite(entity)
        }

    override suspend fun removeFavorite(item: FavoriteItem) =
        withContext(Dispatchers.IO) {
            dao.deleteFavorite(
                itemId = item.itemId,
                type = item.type.name
            )
        }

    override fun observeFavoriteIdsByType(type: FavoriteType): Flow<List<String>> {
        return dao.observeFavoriteIdsByType(type.name).flowOn(Dispatchers.IO)
    }

    override suspend fun isFavorite(itemId: String, type: FavoriteType): Boolean =
        withContext(Dispatchers.IO) {
            dao.isFavorite(itemId, type.name)
        }

    override fun observeIsFavorite(itemId: String, type: FavoriteType): Flow<Boolean> {
        return dao.observeIsFavorite(itemId, type.name).flowOn(Dispatchers.IO)
    }

    override suspend fun toggleFavorite(item: FavoriteItem) =
        withContext(Dispatchers.IO) {
            val currentlyFavorite = dao.isFavorite(item.itemId, item.type.name)
            if (currentlyFavorite) {
                removeFavorite(item)
            } else {
                addFavorite(item)
            }
        }
}
