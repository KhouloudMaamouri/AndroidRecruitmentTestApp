package com.apps.albums

import com.apps.album.error.AlbumError
import com.apps.album.model.Album
import com.apps.album.repository.AlbumRepository
import com.apps.albums.local.AlbumLocalDataSource
import com.apps.albums.mapper.toAlbumDBError
import com.apps.albums.mapper.toAlbumError
import com.apps.albums.mapper.toDomain
import com.apps.albums.mapper.toEntity
import com.apps.albums.remote.AlbumRemoteDataSource
import com.apps.common.result.AppResult
import com.apps.favs.model.FavoriteItem
import com.apps.favs.model.FavoriteType
import com.apps.favs.repository.FavoritesRepository
import com.apps.util.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of [AlbumRepository] coordinating local database caching, remote network operations,
 * and favorite states via [FavoritesRepository].
 *
 * @property localDataSource Local data source for cached album entities.
 * @property remoteDataSource Remote data source for fetching album data from network API.
 * @property favoritesRepository Domain repository managing favorite items.
 */
class AlbumRepositoryImpl @Inject constructor(
    private val localDataSource: AlbumLocalDataSource,
    private val remoteDataSource: AlbumRemoteDataSource,
    private val favoritesRepository: FavoritesRepository,
) : AlbumRepository {

    /**
     * Observes cached albums combined with user favorite status from [FavoritesRepository] with pagination support.
     *
     * @param limit Maximum number of items to return, default is 10.
     * @param offset Number of items to skip, default is 0.
     * @return A [Flow] emitting an [AppResult] with a list of domain [Album]s or a database error.
     */
    override fun observeAlbums(
        limit: Int,
        offset: Int
    ): Flow<AppResult<List<Album>, AlbumError>> {
        return combine(
            localDataSource.observeAlbums(limit = limit, offset = offset),
            favoritesRepository.observeFavoriteIdsByType(FavoriteType.ALBUM)
        ) { entities, favoriteIds ->
            val favSet = favoriteIds.mapNotNull { it.toIntOrNull() }.toSet()
            AppResult.Success(
                entities.map { entity ->
                    entity.toDomain(isFavorite = favSet.contains(entity.id))
                }
            ) as AppResult<List<Album>, AlbumError>
        }.catch { throwable ->
            emit(AppResult.Error(throwable.toAlbumDBError()))
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Observes a single album by ID combined with its favorite status from [FavoritesRepository].
     *
     * @param id Unique identifier of the album item.
     * @return A [Flow] emitting an [AppResult] with the domain [Album] or a database error.
     */
    override fun observeAlbum(id: Int): Flow<AppResult<Album?, AlbumError>> {
        return combine(
            localDataSource.observeAlbum(id),
            favoritesRepository.observeIsFavorite(id.toString(), FavoriteType.ALBUM)
        ) { entity, isFav ->
            AppResult.Success(
                entity?.toDomain(isFavorite = isFav)
            ) as AppResult<Album?, AlbumError>
        }.catch { throwable ->
            emit(AppResult.Error(throwable.toAlbumDBError()))
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Fetches fresh albums from the network and saves them into the local cache.
     *
     * @return An [AppResult] indicating success or network error.
     */
    override suspend fun refreshAlbums(): AppResult<Unit, AlbumError> =
        withContext(Dispatchers.IO) {
            when (val result = remoteDataSource.getAlbums()) {
                is NetworkResult.Success -> {
                    val entities = result.data.map { it.toEntity() }
                    localDataSource.saveAlbums(entities)
                    AppResult.Success(Unit)
                }
                is NetworkResult.Error -> {
                    AppResult.Error(
                        result.error.toAlbumError()
                    )
                }
            }
        }

    /**
     * Toggles the favorite state of an album item via [FavoritesRepository].
     *
     * @param id Unique identifier of the album item.
     */
    override suspend fun toggleFavorite(id: Int) =
        withContext(Dispatchers.IO) {
            favoritesRepository.toggleFavorite(FavoriteItem(itemId = id.toString(), type = FavoriteType.ALBUM))
        }

    /**
     * Observes the favorite status for a given album item via [FavoritesRepository].
     *
     * @param id Unique identifier of the album item.
     * @return A [Flow] emitting true if favorited, false otherwise.
     */
    override fun observeFavorite(id: Int): Flow<Boolean> {
        return favoritesRepository.observeIsFavorite(id.toString(), FavoriteType.ALBUM)
            .flowOn(Dispatchers.IO)
    }
}