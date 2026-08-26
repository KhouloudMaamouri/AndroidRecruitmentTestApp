package com.apps.albums.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apps.album.model.Album
import com.apps.albums.AlbumsIntent


/**
 * Composable rendering a scrollable lazy list of [AlbumItem] components with pagination support.
 *
 * @param albums The list of [Album] items to display.
 * @param isLoadingMore Whether more items are currently being loaded.
 * @param canLoadMore Whether more items are available to be loaded.
 * @param onIntent Callback for handling user intents when selecting or favoring items.
 */
@Composable
fun AlbumsContent(
    albums: List<Album>,
    isLoadingMore: Boolean = false,
    canLoadMore: Boolean = true,
    onIntent: (AlbumsIntent) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    val shouldLoadMore = remember(lazyListState, isLoadingMore, canLoadMore, albums.size) {
        derivedStateOf {
            val totalItemsCount = lazyListState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            !isLoadingMore && canLoadMore && totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onIntent.invoke(AlbumsIntent.LoadMore)
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = albums,
            key = { album -> album.id }
        ) { album ->

            AlbumItem(
                album = album,
                onItemSelected = { albumSelected ->
                    onIntent.invoke(AlbumsIntent.AlbumClicked(albumSelected))
                },
                onFavoriteToggle = {
                    onIntent.invoke(AlbumsIntent.ToggleFavorite(album.id))
                }
            )
        }

        if (isLoadingMore) {
            item(key = "loading_more_indicator") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}