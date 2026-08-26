package com.apps.impl

import android.content.Context
import androidx.room.Room
import com.apps.albums.local.AlbumDao
import com.apps.database.DatabaseTransactionRunner
import com.apps.favorites.local.FavoritesDao
import com.apps.impl.di.DatabaseTransactionRunnerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for building and providing Room database, DAO, and transaction runner instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton instance of [AppDatabase].
     *
     * @param context Application context for Room database builder.
     * @return The constructed [AppDatabase].
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app.db",
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    /**
     * Provides a singleton instance of [AlbumDao] from the [AppDatabase].
     *
     * @param database The [AppDatabase] instance.
     * @return The [AlbumDao] instance.
     */
    @Provides
    @Singleton
    fun provideAlbumDao(
        database: AppDatabase
    ): AlbumDao {
        return database.albumDao()
    }

    /**
     * Provides a singleton instance of [FavoritesDao] from the [AppDatabase].
     *
     * @param database The [AppDatabase] instance.
     * @return The [FavoritesDao] instance.
     */
    @Provides
    @Singleton
    fun provideFavoritesDao(
        database: AppDatabase
    ): FavoritesDao {
        return database.favoritesDao()
    }

    /**
     * Provides a singleton instance of [DatabaseTransactionRunner].
     *
     * @param database The [AppDatabase] instance.
     * @return The [DatabaseTransactionRunner] instance.
     */
    @Provides
    @Singleton
    fun provideDatabaseTransactionRunner(
        database: AppDatabase
    ): DatabaseTransactionRunner {
        return DatabaseTransactionRunnerImpl(database)
    }
}