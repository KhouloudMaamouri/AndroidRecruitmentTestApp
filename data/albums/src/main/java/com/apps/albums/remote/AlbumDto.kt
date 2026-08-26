package com.apps.albums.remote

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for parsing album JSON responses from the remote API.
 *
 * @property id Item unique identifier.
 * @property albumId Parent album identifier.
 * @property title Album item title.
 * @property url Full image URL.
 * @property thumbnailUrl Thumbnail image URL.
 */
@Serializable
data class AlbumDto(
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)