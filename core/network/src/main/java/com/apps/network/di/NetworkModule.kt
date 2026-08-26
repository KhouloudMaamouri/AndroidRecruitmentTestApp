package com.apps.network.di

import com.apps.network.NetworkConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt module for providing core network dependencies ([Json] serializer and [Retrofit] HTTP client).
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a configured [Json] instance ignoring unknown JSON keys.
     *
     * @return The configured [Json] instance.
     */
    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
        }

    /**
     * Provides a singleton instance of [Retrofit] configured with base URL and Kotlinx Serialization converter.
     *
     * @param json The [Json] serializer instance.
     * @return The constructed [Retrofit] client.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        json: Json
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
}