package com.apps.albums.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.ImageRequest
import okhttp3.OkHttpClient

/**
 * Composable thumbnail component for loading album images via Coil with a custom User-Agent header.
 *
 * @param url Remote image URL to load.
 * @param contentDescription Accessibility description for the image.
 * @param modifier Layout modifier applied to the image.
 */
@Composable
fun AlbumThumbnail(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(
                OkHttpNetworkFetcherFactory(
                    callFactory = {
                        OkHttpClient.Builder()
                            .addInterceptor { chain ->
                                val request = chain.request()
                                    .newBuilder()
                                    .addHeader("User-Agent", "LeboncoinApp/1.0")
                                    .build()

                                chain.proceed(request)
                            }
                            .build()
                    }
                )
            )
        }
        .build()


    AsyncImage(
        model = url,
        imageLoader = imageLoader,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}