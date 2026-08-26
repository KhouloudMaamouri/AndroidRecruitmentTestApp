package com.apps.albums.di

import com.apps.albums.local.AlbumLocalDataSource
import com.apps.albums.local.AlbumLocalDataSourceImpl
import com.apps.albums.remote.AlbumRemoteDataSource
import com.apps.albums.remote.AlbumRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Hilt module binding implementation classes for local and remote album data sources.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumDataModule {

    /**
     * Binds the [AlbumRemoteDataSourceImpl] implementation to [AlbumRemoteDataSource].
     *
     * @param implementation The concrete remote data source implementation.
     * @return The bound [AlbumRemoteDataSource].
     */
    @Binds
    @Singleton
    abstract fun bindAlbumRemoteDataSource(
        implementation: AlbumRemoteDataSourceImpl
    ): AlbumRemoteDataSource

    /**
     * Binds the [AlbumLocalDataSourceImpl] implementation to [AlbumLocalDataSource].
     *
     * @param implementation The concrete local data source implementation.
     * @return The bound [AlbumLocalDataSource].
     */
    @Binds
    @Singleton
    abstract fun bindAlbumLocalDataSource(
        implementation: AlbumLocalDataSourceImpl
    ): AlbumLocalDataSource
}