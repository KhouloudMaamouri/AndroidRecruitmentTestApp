package com.apps.albums.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing an album item record stored in the "albums" database table.
 *
 * @property id Primary key and unique identifier of the album item.
 * @property albumId Grouping identifier of the album.
 * @property title Title text of the album item.
 * @property url Full image URL string.
 * @property thumbnailUrl Thumbnail image URL string.
 */
@Entity(
    tableName = "albums",
)
data class AlbumEntity(
    @PrimaryKey
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)