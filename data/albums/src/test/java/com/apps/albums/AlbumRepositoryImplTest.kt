package com.apps.albums

import com.apps.album.error.AlbumError
import com.apps.albums.local.AlbumEntity
import com.apps.albums.local.AlbumLocalDataSource
import com.apps.albums.remote.AlbumDto
import com.apps.albums.remote.AlbumRemoteDataSource
import com.apps.common.result.AppResult
import com.apps.favs.model.FavoriteItem
import com.apps.favs.model.FavoriteType
import com.apps.favs.repository.FavoritesRepository
import com.apps.network.model.NetworkError
import com.apps.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [AlbumRepositoryImpl].
 */
class AlbumRepositoryImplTest {

    private val sampleEntity = AlbumEntity(
        id = 1,
        albumId = 10,
        title = "Repo Test Album",
        url = "https://example.com/1.png",
        thumbnailUrl = "https://example.com/1_thumb.png"
    )

    private val sampleDto = AlbumDto(
        id = 1,
        albumId = 10,
        title = "Repo Test Album",
        url = "https://example.com/1.png",
        thumbnailUrl = "https://example.com/1_thumb.png"
    )

    private val fakeLocalDataSource = object : AlbumLocalDataSource {
        val entitiesFlow = MutableStateFlow<List<AlbumEntity>>(listOf(sampleEntity))
        val savedEntities = mutableListOf<AlbumEntity>()

        override fun observeAlbums(limit: Int, offset: Int): Flow<List<AlbumEntity>> = entitiesFlow
        override fun observeAlbum(id: Int): Flow<AlbumEntity?> =
            flowOf(entitiesFlow.value.find { it.id == id })

        override suspend fun saveAlbums(albums: List<AlbumEntity>) {
            savedEntities.clear()
            savedEntities.addAll(albums)
            entitiesFlow.value = albums
        }
    }

    private val fakeRemoteDataSource = object : AlbumRemoteDataSource {
        var networkResult: NetworkResult<List<AlbumDto>> = NetworkResult.Success(listOf(sampleDto))

        override suspend fun getAlbums(): NetworkResult<List<AlbumDto>> = networkResult
    }

    private val fakeFavoritesRepository = object : FavoritesRepository {
        val favoriteIds = MutableStateFlow(listOf("1"))
        val toggledItems = mutableListOf<FavoriteItem>()

        override suspend fun addFavorite(item: FavoriteItem) {}
        override suspend fun removeFavorite(item: FavoriteItem) {}
        override fun observeFavoriteIdsByType(type: FavoriteType): Flow<List<String>> = favoriteIds
        override suspend fun isFavorite(itemId: String, type: FavoriteType): Boolean =
            favoriteIds.value.contains(itemId)

        override fun observeIsFavorite(itemId: String, type: FavoriteType): Flow<Boolean> =
            flowOf(favoriteIds.value.contains(itemId))

        override suspend fun toggleFavorite(item: FavoriteItem) {
            toggledItems.add(item)
        }
    }

    @Test
    fun observeAlbums_combinesLocalEntitiesAndFavoriteIds() = runTest {
        val repository = AlbumRepositoryImpl(
            localDataSource = fakeLocalDataSource,
            remoteDataSource = fakeRemoteDataSource,
            favoritesRepository = fakeFavoritesRepository,
        )

        val result = repository.observeAlbums().first()

        assertTrue(result is AppResult.Success)
        val albums = (result as AppResult.Success).data
        assertEquals(1, albums.size)
        assertEquals(1, albums.first().id)
        assertTrue(albums.first().isFavorite)
    }

    @Test
    fun observeAlbum_withValidId_returnsDomainAlbumWithFavoriteStatus() = runTest {
        val repository = AlbumRepositoryImpl(
            localDataSource = fakeLocalDataSource,
            remoteDataSource = fakeRemoteDataSource,
            favoritesRepository = fakeFavoritesRepository,
        )

        val result = repository.observeAlbum(1).first()

        assertTrue(result is AppResult.Success)
        val album = (result as AppResult.Success).data
        assertEquals(1, album?.id)
        assertTrue(album?.isFavorite == true)
    }

    @Test
    fun refreshAlbums_success_savesEntitiesToLocalDataSource() = runTest {
        val repository = AlbumRepositoryImpl(
            localDataSource = fakeLocalDataSource,
            remoteDataSource = fakeRemoteDataSource,
            favoritesRepository = fakeFavoritesRepository,
        )

        val result = repository.refreshAlbums()

        assertTrue(result is AppResult.Success)
        assertEquals(1, fakeLocalDataSource.savedEntities.size)
        assertEquals("Repo Test Album", fakeLocalDataSource.savedEntities.first().title)
    }

    @Test
    fun refreshAlbums_failure_returnsMappedAlbumError() = runTest {
        fakeRemoteDataSource.networkResult = NetworkResult.Error(NetworkError.NoInternet)

        val repository = AlbumRepositoryImpl(
            localDataSource = fakeLocalDataSource,
            remoteDataSource = fakeRemoteDataSource,
            favoritesRepository = fakeFavoritesRepository,
        )

        val result = repository.refreshAlbums()

        assertTrue(result is AppResult.Error)
        assertEquals(AlbumError.NetworkUnavailable, (result as AppResult.Error).error)
    }

    @Test
    fun toggleFavorite_delegatesToFavoritesRepository() = runTest {
        val repository = AlbumRepositoryImpl(
            localDataSource = fakeLocalDataSource,
            remoteDataSource = fakeRemoteDataSource,
            favoritesRepository = fakeFavoritesRepository,
        )

        repository.toggleFavorite(42)

        assertEquals(1, fakeFavoritesRepository.toggledItems.size)
        assertEquals("42", fakeFavoritesRepository.toggledItems.first().itemId)
        assertEquals(FavoriteType.ALBUM, fakeFavoritesRepository.toggledItems.first().type)
    }
}
