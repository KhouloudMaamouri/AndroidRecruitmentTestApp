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
 * Unit tests for [ToggleFavoriteUseCase].
 */
class ToggleFavoriteUseCaseTest {

    private val fakeRepository = object : FavoritesRepository {
        val toggledItems = mutableListOf<FavoriteItem>()

        override suspend fun addFavorite(item: FavoriteItem) {}
        override suspend fun removeFavorite(item: FavoriteItem) {}
        override fun observeFavoriteIdsByType(type: FavoriteType): Flow<List<String>> = flowOf(emptyList())
        override suspend fun isFavorite(itemId: String, type: FavoriteType): Boolean = false
        override fun observeIsFavorite(itemId: String, type: FavoriteType): Flow<Boolean> = flowOf(false)

        override suspend fun toggleFavorite(item: FavoriteItem) {
            toggledItems.add(item)
        }
    }

    @Test
    fun invoke_delegatesToRepositoryToggle() = runTest {
        val useCase = ToggleFavoriteUseCase(fakeRepository)
        val item = FavoriteItem(itemId = "123", type = FavoriteType.ALBUM)

        useCase(item)

        assertEquals(1, fakeRepository.toggledItems.size)
        assertEquals(item, fakeRepository.toggledItems.first())
    }
}
