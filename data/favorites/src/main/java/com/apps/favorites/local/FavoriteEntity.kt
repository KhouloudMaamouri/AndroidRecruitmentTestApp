package com.apps.favorites.local

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Room database entity representing a favorite item.
 * Utilizes a composite primary key consisting of [itemId] and [type].
 *
 * @property itemId Unique identifier of the favorite item.
 * @property type String representation of the [FavoriteType] enum.
 */
@Entity(
    tableName = "favorites",
    primaryKeys = ["itemId", "type"]
)
data class FavoriteEntity(
    @ColumnInfo(name = "itemId")
    val itemId: String,
    @ColumnInfo(name = "type")
    val type: String
)
