package com.apps.album.model

/**
 * Domain model representing an album entry.
 *
 * @property id Unique identifier of the album item.
 * @property albumId Grouping identifier of the parent album.
 * @property title Title text of the album item.
 * @property url Full image URL for detailed view.
 * @property thumbnailUrl Thumbnail image URL for list view.
 * @property isFavorite Whether the user has marked this album as favorite.
 */
data class Album(
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
    val isFavorite: Boolean = false,
)