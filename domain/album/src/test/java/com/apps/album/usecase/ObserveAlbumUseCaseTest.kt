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
 * Unit tests for [ObserveAlbumUseCase].
 */
class ObserveAlbumUseCaseTest {

    private val sampleAlbum = Album(
        id = 42,
        albumId = 2,
        title = "Single Album Test",
        url = "https://example.com/img42.png",
        thumbnailUrl = "https://example.com/thumb42.png",
        isFavorite = true
    )

    private val fakeRepository = object : AlbumRepository {
        override fun observeAlbums(limit: Int, offset: Int): Flow<AppResult<List<Album>, AlbumError>> =
            flowOf(AppResult.Success(listOf(sampleAlbum)))

        override fun observeAlbum(id: Int): Flow<AppResult<Album?, AlbumError>> =
            flowOf(
                if (id == -1) AppResult.Error(AlbumError.DatabaseError)
                else AppResult.Success(if (id == 42) sampleAlbum else null)
            )

        override suspend fun refreshAlbums(): AppResult<Unit, AlbumError> = AppResult.Success(Unit)
        override suspend fun toggleFavorite(id: Int) {}
        override fun observeFavorite(id: Int): Flow<Boolean> = flowOf(id == 42)
    }

    @Test
    fun invoke_withValidId_returnsAlbum() = runTest {
        val useCase = ObserveAlbumUseCase(fakeRepository)
        val result = useCase(42).first()

        assertTrue(result is AppResult.Success)
        val album = (result as AppResult.Success).data
        assertEquals(42, album?.id)
        assertEquals("Single Album Test", album?.title)
    }

    @Test
    fun invoke_withInvalidId_returnsNullAlbum() = runTest {
        val useCase = ObserveAlbumUseCase(fakeRepository)
        val result = useCase(999).first()

        assertTrue(result is AppResult.Success)
        val album = (result as AppResult.Success).data
        assertEquals(null, album)
    }

    @Test
    fun invoke_errorState_returnsError() = runTest {
        val useCase = ObserveAlbumUseCase(fakeRepository)
        val result = useCase(-1).first()

        assertTrue(result is AppResult.Error)
        assertEquals(AlbumError.DatabaseError, (result as AppResult.Error).error)
    }
}
