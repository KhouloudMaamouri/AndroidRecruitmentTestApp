package com.apps.favorites.local

import androidx.room.TypeConverter
import com.apps.favs.model.FavoriteType

/**
 * Room TypeConverter mapping [FavoriteType] enum values to string representations and vice-versa.
 */
class FavoriteTypeConverter {

    /**
     * Converts a [FavoriteType] enum instance to a [String] for Room database persistence.
     *
     * @param type The [FavoriteType] enum instance.
     * @return The string representation of the enum.
     */
    @TypeConverter
    fun fromFavoriteType(type: FavoriteType): String {
        return type.name
    }

    /**
     * Converts a database [String] back into its corresponding [FavoriteType] enum instance.
     *
     * @param value The string value retrieved from the database.
     * @return The mapped [FavoriteType] enum instance.
     */
    @TypeConverter
    fun toFavoriteType(value: String): FavoriteType {
        return try {
            FavoriteType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            FavoriteType.ALBUM
        }
    }
}
