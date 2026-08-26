package com.apps.album_detail

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Stateful entry point route for displaying an album's detail screen.
 *
 * Listens to `albumId` changes, triggers data loading, and binds state to [AlbumDetailScreen].
 *
 * @param albumId Unique identifier of the album item to display.
 * @param viewModel The Hilt-injected [AlbumDetailViewModel].
 */
@Composable
fun AlbumDetailRoute(
    albumId: Int,
    viewModel: AlbumDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(albumId) {
        viewModel.onIntent(AlbumDetailIntent.Load(albumId))
    }

    AlbumDetailScreen(
        state = state,
        onIntent = viewModel::onIntent
    )
}