package com.apps.albums

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Stateful entry point route for the Albums feature screen.
 *
 * Collects state and side effects from [AlbumsViewModel] and binds them to [AlbumsScreen].
 *
 * @param onAlbumSelected Callback invoked when an album item is selected for navigation.
 * @param viewModel The Hilt-injected [AlbumsViewModel] instance.
 */
@Composable
fun AlbumsRoute(
    onAlbumSelected: (Int) -> Unit,
    viewModel: AlbumsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(AlbumsIntent.Load)
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is AlbumsEffect.NavigateToAlbum -> {
                    onAlbumSelected(effect.albumId)
                }

                is AlbumsEffect.ShowError -> {
                    Toast.makeText(context, com.apps.ui.R.string.error_unknown, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AlbumsScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}