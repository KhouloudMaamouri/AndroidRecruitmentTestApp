package com.apps.albums.di

import com.apps.albums.remote.AlbumApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt module for providing network API dependencies for album features.
 */
@Module
@InstallIn(SingletonComponent::class)
object AlbumApiModule {

    /**
     * Provides a singleton instance of [AlbumApi] using [Retrofit].
     *
     * @param retrofit The configured [Retrofit] instance.
     * @return The created [AlbumApi] service.
     */
    @Provides
    @Singleton
    fun provideAlbumApi(
        retrofit: Retrofit
    ): AlbumApi {
        return retrofit.create(AlbumApi::class.java)
    }
}