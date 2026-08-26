package com.apps.favorites.repository


import com.apps.favorites.local.FavoriteEntity
import com.apps.favorites.local.FavoritesDao
import com.apps.favs.model.FavoriteItem
import com.apps.favs.model.FavoriteType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.coroutines.test.runTest

/**
 * Unit tests for [FavoritesRepositoryImpl].
 */
class FavoritesRepositoryImplTest {

    private val fakeDao = object : FavoritesDao {
        val favorites = MutableStateFlow<List<FavoriteEntity>>(emptyList())

        override suspend fun insertFavorite(favorite: FavoriteEntity) {
            favorites.value += favorite
        }

        override suspend fun deleteFavorite(itemId: String, type: String) {
            favorites.value = favorites.value.filterNot { it.itemId == itemId && it.type == type }
        }

        override fun observeFavoriteIdsByType(type: String): Flow<List<String>> {
            return favorites.map { list ->
                list.filter { it.type == type }.map { it.itemId }
            }
        }

        override suspend fun isFavorite(itemId: String, type: String): Boolean {
            return favorites.value.any { it.itemId == itemId && it.type == type }
        }

        override fun observeIsFavorite(itemId: String, type: String): Flow<Boolean> {
            return favorites.map { list ->
                list.any { it.itemId == itemId && it.type == type }
            }
        }
    }

    @Test
    fun addFavorite_addsEntityToDao() = runTest {
        val repository = FavoritesRepositoryImpl(fakeDao)
        val item = FavoriteItem(itemId = "fav_1", type = FavoriteType.ALBUM)

        repository.addFavorite(item)

        assertTrue(fakeDao.isFavorite("fav_1", FavoriteType.ALBUM.name))
    }

    @Test
    fun removeFavorite_removesEntityFromDao() = runTest {
        val repository = FavoritesRepositoryImpl(fakeDao)
        val item = FavoriteItem(itemId = "fav_2", type = FavoriteType.ALBUM)

        repository.addFavorite(item)
        assertTrue(fakeDao.isFavorite("fav_2", FavoriteType.ALBUM.name))

        repository.removeFavorite(item)
        assertFalse(fakeDao.isFavorite("fav_2", FavoriteType.ALBUM.name))
    }

    @Test
    fun toggleFavorite_togglesFavoriteState() = runTest {
        val repository = FavoritesRepositoryImpl(fakeDao)
        val item = FavoriteItem(itemId = "fav_3", type = FavoriteType.ALBUM)

        // First toggle: adds to favorite
        repository.toggleFavorite(item)
        assertTrue(repository.isFavorite("fav_3", FavoriteType.ALBUM))

        // Second toggle: removes from favorite
        repository.toggleFavorite(item)
        assertFalse(repository.isFavorite("fav_3", FavoriteType.ALBUM))
    }

    @Test
    fun observeFavoriteIdsByType_emitsMatchingIds() = runTest {
        val repository = FavoritesRepositoryImpl(fakeDao)
        repository.addFavorite(FavoriteItem(itemId = "10", type = FavoriteType.ALBUM))
        repository.addFavorite(FavoriteItem(itemId = "20", type = FavoriteType.ALBUM))

        val ids = repository.observeFavoriteIdsByType(FavoriteType.ALBUM).first()

        assertEquals(listOf("10", "20"), ids)
    }
}
