package com.apps.favs.model

/**
 * Domain model representing a favorited item with a unique item identifier and category type.
 *
 * @property itemId Unique string identifier of the item.
 * @property type The [FavoriteType] category of the item.
 */
data class FavoriteItem(
    val itemId: String,
    val type: FavoriteType
)
