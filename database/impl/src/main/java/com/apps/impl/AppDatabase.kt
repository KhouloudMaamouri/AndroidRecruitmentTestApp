package com.apps.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apps.albums.local.AlbumDao
import com.apps.albums.local.AlbumEntity
import com.apps.favorites.local.FavoriteEntity
import com.apps.favorites.local.FavoriteTypeConverter
import com.apps.favorites.local.FavoritesDao

/**
 * Primary Room database instance for the application storing albums and user favorites.
 */
@Database(
    entities = [
        AlbumEntity::class,
        FavoriteEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(FavoriteTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides the [AlbumDao] for album database queries.
     *
     * @return The [AlbumDao] instance.
     */
    abstract fun albumDao(): AlbumDao

    /**
     * Provides the [FavoritesDao] for generic favorites database queries.
     *
     * @return The [FavoritesDao] instance.
     */
    abstract fun favoritesDao(): FavoritesDao
}