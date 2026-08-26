package com.apps.albums.di

import com.apps.album.repository.AlbumRepository
import com.apps.albums.AlbumRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module binding [AlbumRepositoryImpl] to the domain [AlbumRepository] interface.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumRepositoryModule {

    /**
     * Binds the repository implementation to the domain interface.
     *
     * @param implementation The concrete repository implementation.
     * @return The bound [AlbumRepository].
     */
    @Binds
    @Singleton
    abstract fun bindAlbumRepository(
        implementation: AlbumRepositoryImpl
    ): AlbumRepository
}