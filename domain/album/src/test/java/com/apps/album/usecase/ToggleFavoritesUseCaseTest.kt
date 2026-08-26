package com.apps.album.usecase

import com.apps.album.error.AlbumError
import com.apps.album.model.Album
import com.apps.album.repository.AlbumRepository
import com.apps.common.result.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [ToggleFavoritesUseCase].
 */
class ToggleFavoritesUseCaseTest {

    private val fakeRepository = object : AlbumRepository {
        var toggledId: Int? = null

        override fun observeAlbums(limit: Int, offset: Int): Flow<AppResult<List<Album>, AlbumError>> =
            flowOf(AppResult.Success(emptyList()))

        override fun observeAlbum(id: Int): Flow<AppResult<Album?, AlbumError>> =
            flowOf(AppResult.Success(null))

        override suspend fun refreshAlbums(): AppResult<Unit, AlbumError> =
            AppResult.Success(Unit)

        override suspend fun toggleFavorite(id: Int) {
            toggledId = id
        }

        override fun observeFavorite(id: Int): Flow<Boolean> = flowOf(false)
    }

    @Test
    fun invoke_delegatesToRepositoryToggleFavorite() = runTest {
        val useCase = ToggleFavoritesUseCase(fakeRepository)
        useCase(101)

        assertEquals(101, fakeRepository.toggledId)
    }
}
