package com.apps.albums

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.apps.albums.components.AlbumsContent
import com.apps.albums.components.ErrorContent
/**
 * Stateless UI screen composable for displaying album list, loading indicator, or error content.
 *
 * @param state Current [AlbumsUiState] containing loaded albums, loading state, or error.
 * @param onIntent Callback for dispatching user [AlbumsIntent] actions.
 */
@Composable
fun AlbumsScreen(
    state: AlbumsUiState,
    onIntent: (AlbumsIntent) -> Unit,
) {
    when {
        state.isLoading || (state.isRefreshing && state.albums.isEmpty()) -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null && state.albums.isEmpty() -> {
            ErrorContent(
                message = state.error,
                onRetry = { onIntent.invoke(AlbumsIntent.Retry) }
            )
        }

        else -> {
            AlbumsContent(
                albums = state.albums,
                isLoadingMore = state.isLoadingMore,
                canLoadMore = state.canLoadMore,
                onIntent = onIntent,
            )
        }
    }
}