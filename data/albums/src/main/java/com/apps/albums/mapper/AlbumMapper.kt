package com.apps.albums.mapper

import android.database.sqlite.SQLiteException
import com.apps.album.error.AlbumError
import com.apps.album.model.Album
import com.apps.albums.local.AlbumEntity
import com.apps.albums.remote.AlbumDto
import com.apps.network.model.NetworkError

/**
 * Maps an [AlbumEntity] to its domain model [Album].
 *
 * @param isFavorite Whether the album item is currently marked as favorite.
 * @return The mapped domain [Album].
 */
fun AlbumEntity.toDomain(isFavorite: Boolean = false): Album {
    return Album(
        id = id,
        albumId = albumId,
        title = title,
        url = url,
        thumbnailUrl = thumbnailUrl,
        isFavorite = isFavorite,
    )
}

/**
 * Maps a network [AlbumDto] to a database [AlbumEntity].
 *
 * @return The mapped database [AlbumEntity].
 */
fun AlbumDto.toEntity(): AlbumEntity {
    return AlbumEntity(
        id = id,
        albumId = albumId,
        title = title,
        url = url,
        thumbnailUrl = thumbnailUrl
    )
}

/**
 * Maps a network layer [NetworkError] to a domain-level [AlbumError].
 *
 * @return The corresponding domain [AlbumError].
 */
fun NetworkError.toAlbumError(): AlbumError {
    return when (this) {
        NetworkError.NoInternet -> AlbumError.NetworkUnavailable
        NetworkError.Timeout -> AlbumError.NetworkUnavailable
        is NetworkError.Server ->  AlbumError.ServerError
        is NetworkError.Unknown -> AlbumError.Unknown
        else -> AlbumError.Unknown
    }
}

/**
 * Maps a database exception to a domain-level [AlbumError].
 *
 * @return The mapped [AlbumError].
 */
fun Throwable.toAlbumDBError(): AlbumError {
    return when (this) {
        is SQLiteException -> AlbumError.DatabaseError
        else -> AlbumError.Unknown
    }
}