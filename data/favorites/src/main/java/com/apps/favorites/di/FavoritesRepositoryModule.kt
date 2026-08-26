package com.apps.favorites.di


import com.apps.favorites.repository.FavoritesRepositoryImpl
import com.apps.favs.repository.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt DI module binding [FavoritesRepositoryImpl] to domain [FavoritesRepository].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class FavoritesRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        impl: FavoritesRepositoryImpl
    ): FavoritesRepository
}
