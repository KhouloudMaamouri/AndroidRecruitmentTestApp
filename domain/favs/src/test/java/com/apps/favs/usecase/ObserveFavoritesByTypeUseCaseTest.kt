package com.apps.favs.usecase


import com.apps.favs.model.FavoriteItem
import com.apps.favs.model.FavoriteType
import com.apps.favs.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [ObserveFavoritesByTypeUseCase].
 */
class ObserveFavoritesByTypeUseCaseTest {

    private val fakeRepository = object : FavoritesRepository {
        override suspend fun addFavorite(item: FavoriteItem) {}
        override suspend fun removeFavorite(item: FavoriteItem) {}
        override fun observeFavoriteIdsByType(type: FavoriteType): Flow<List<String>> {
            return flowOf(listOf("item1", "item2"))
        }
        override suspend fun isFavorite(itemId: String, type: FavoriteType): Boolean = false
        override fun observeIsFavorite(itemId: String, type: FavoriteType): Flow<Boolean> = flowOf(false)
        override suspend fun toggleFavorite(item: FavoriteItem) {}
    }

    @Test
    fun invoke_returnsFavoriteIdsFromRepository() = runTest {
        val useCase = ObserveFavoritesByTypeUseCase(fakeRepository)
        val result = useCase(FavoriteType.ALBUM).first()

        assertEquals(2, result.size)
        assertEquals(listOf("item1", "item2"), result)
    }
}
