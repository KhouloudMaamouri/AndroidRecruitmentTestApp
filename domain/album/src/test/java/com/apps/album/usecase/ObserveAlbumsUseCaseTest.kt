package com.apps.album.usecase

import com.apps.album.error.AlbumError
import com.apps.album.model.Album
import com.apps.album.repository.AlbumRepository
import com.apps.common.result.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [ObserveAlbumsUseCase].
 */
class ObserveAlbumsUseCaseTest {

    private val sampleAlbum = Album(
        id = 1,
        albumId = 10,
        title = "Album Test",
        url = "https://example.com/img.png",
        thumbnailUrl = "https://example.com/thumb.png",
        isFavorite = false
    )

    private val fakeSuccessRepository = object : AlbumRepository {
        override fun observeAlbums(limit: Int, offset: Int): Flow<AppResult<List<Album>, AlbumError>> =
            flowOf(AppResult.Success(listOf(sampleAlbum)))

        override fun observeAlbum(id: Int): Flow<AppResult<Album?, AlbumError>> =
            flowOf(AppResult.Success(sampleAlbum))

        override suspend fun refreshAlbums(): AppResult<Unit, AlbumError> = AppResult.Success(Unit)
        override suspend fun toggleFavorite(id: Int) {}
        override fun observeFavorite(id: Int): Flow<Boolean> = flowOf(false)
    }

    private val fakeErrorRepository = object : AlbumRepository {
        override fun observeAlbums(limit: Int, offset: Int): Flow<AppResult<List<Album>, AlbumError>> =
            flowOf(AppResult.Error(AlbumError.DatabaseError))

        override fun observeAlbum(id: Int): Flow<AppResult<Album?, AlbumError>> =
            flowOf(AppResult.Error(AlbumError.DatabaseError))

        override suspend fun refreshAlbums(): AppResult<Unit, AlbumError> = AppResult.Error(AlbumError.NetworkUnavailable)
        override suspend fun toggleFavorite(id: Int) {}
        override fun observeFavorite(id: Int): Flow<Boolean> = flowOf(false)
    }

    @Test
    fun invoke_success_returnsAlbumsFromRepository() = runTest {
        val useCase = ObserveAlbumsUseCase(fakeSuccessRepository)
        val result = useCase().first()

        assertTrue(result is AppResult.Success)
        val albums = (result as AppResult.Success).data
        assertEquals(1, albums.size)
        assertEquals("Album Test", albums.first().title)
    }

    @Test
    fun invoke_error_returnsDatabaseErrorFromRepository() = runTest {
        val useCase = ObserveAlbumsUseCase(fakeErrorRepository)
        val result = useCase().first()

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertEquals(AlbumError.DatabaseError, error)
    }
}
