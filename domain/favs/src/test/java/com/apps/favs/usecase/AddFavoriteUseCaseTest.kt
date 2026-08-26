package com.apps.favs.usecase

import com.apps.favs.model.FavoriteItem
import com.apps.favs.model.FavoriteType
import com.apps.favs.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [AddFavoriteUseCase].
 */
class AddFavoriteUseCaseTest {

    private val fakeRepository = object : FavoritesRepository {
        val addedItems = mutableListOf<FavoriteItem>()

        override suspend fun addFavorite(item: FavoriteItem) {
            addedItems.add(item)
        }

        override suspend fun removeFavorite(item: FavoriteItem) {}
        override fun observeFavoriteIdsByType(type: FavoriteType): Flow<List<String>> = flowOf(emptyList())
        override suspend fun isFavorite(itemId: String, type: FavoriteType): Boolean = false
        override fun observeIsFavorite(itemId: String, type: FavoriteType): Flow<Boolean> = flowOf(false)
        override suspend fun toggleFavorite(item: FavoriteItem) {}
    }

    @Test
    fun invoke_delegatesToRepositoryAddFavorite() = runTest {
        val useCase = AddFavoriteUseCase(fakeRepository)
        val item = FavoriteItem(itemId = "item_10", type = FavoriteType.ALBUM)

        useCase(item)

        assertEquals(1, fakeRepository.addedItems.size)
        assertEquals(item, fakeRepository.addedItems.first())
    }
}
