package com.apps.album.usecase

import com.apps.album.error.AlbumError
import com.apps.album.model.Album
import com.apps.album.repository.AlbumRepository
import com.apps.common.result.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [RefreshAlbumsUseCase].
 */
class RefreshAlbumsUseCaseTest {

    private val fakeSuccessRepository = object : AlbumRepository {
        var refreshCalled = false

        override fun observeAlbums(limit: Int, offset: Int): Flow<AppResult<List<Album>, AlbumError>> =
            flowOf(AppResult.Success(emptyList()))

        override fun observeAlbum(id: Int): Flow<AppResult<Album?, AlbumError>> =
            flowOf(AppResult.Success(null))

        override suspend fun refreshAlbums(): AppResult<Unit, AlbumError> {
            refreshCalled = true
            return AppResult.Success(Unit)
        }

        override suspend fun toggleFavorite(id: Int) {}
        override fun observeFavorite(id: Int): Flow<Boolean> = flowOf(false)
    }

    private val fakeErrorRepository = object : AlbumRepository {
        override fun observeAlbums(limit: Int, offset: Int): Flow<AppResult<List<Album>, AlbumError>> =
            flowOf(AppResult.Success(emptyList()))

        override fun observeAlbum(id: Int): Flow<AppResult<Album?, AlbumError>> =
            flowOf(AppResult.Success(null))

        override suspend fun refreshAlbums(): AppResult<Unit, AlbumError> =
            AppResult.Error(AlbumError.NetworkUnavailable)

        override suspend fun toggleFavorite(id: Int) {}
        override fun observeFavorite(id: Int): Flow<Boolean> = flowOf(false)
    }

    @Test
    fun invoke_success_delegatesToRepositoryRefreshAlbums() = runTest {
        val useCase = RefreshAlbumsUseCase(fakeSuccessRepository)
        val result = useCase()

        assertTrue(fakeSuccessRepository.refreshCalled)
        assertTrue(result is AppResult.Success)
        assertEquals(Unit, (result as AppResult.Success).data)
    }

    @Test
    fun invoke_failure_returnsNetworkUnavailableError() = runTest {
        val useCase = RefreshAlbumsUseCase(fakeErrorRepository)
        val result = useCase()

        assertTrue(result is AppResult.Error)
        assertEquals(AlbumError.NetworkUnavailable, (result as AppResult.Error).error)
    }
}
