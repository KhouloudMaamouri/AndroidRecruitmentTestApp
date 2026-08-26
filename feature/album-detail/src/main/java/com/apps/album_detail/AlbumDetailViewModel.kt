package com.apps.album_detail

import androidx.lifecycle.viewModelScope
import com.apps.album.usecase.ObserveAlbumUseCase
import com.apps.album.usecase.ToggleFavoritesUseCase
import com.apps.analytics.AnalyticsTracker
import com.apps.common.result.AppResult
import com.apps.logger.AppLogger
import com.apps.mvi.MviViewModel
import com.apps.ui.mapper.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel managing state and user intents for the Album Details screen.
 *
 * @property observeAlbum Use case for observing a specific album's data.
 * @property toggleFavorite Use case for toggling favorite status.
 * @property logger Application logger instance.
 * @property analyticsTracker Analytics tracker for logging user interaction events.
 */
@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val observeAlbum: ObserveAlbumUseCase,
    private val toggleFavorite: ToggleFavoritesUseCase,
    private val logger: AppLogger,
    private val analyticsTracker: AnalyticsTracker,
) : MviViewModel<AlbumDetailIntent, AlbumDetailUiState>(initialState = AlbumDetailUiState()) {

    private var currentAlbumId: Int? = null

    /**
     * Processes user intents on the Album Details screen.
     *
     * @param intent The [AlbumDetailIntent] to execute.
     */
    override fun handleIntent(intent: AlbumDetailIntent) {
        when (intent) {
            is AlbumDetailIntent.Load -> {
                currentAlbumId = intent.albumId
                loadAlbum(intent.albumId)
            }

            AlbumDetailIntent.Retry -> {
                currentAlbumId?.let { loadAlbum(it) }
            }

            AlbumDetailIntent.ToggleFavorite -> {
                val album = state.value.album ?: return
                viewModelScope.launch {
                    toggleFavorite(album.id)
                }
            }
        }
    }

    private fun loadAlbum(albumId: Int) {
        updateState { copy(isLoading = true, error = null) }
        viewModelScope.launch {
            observeAlbum(albumId).collect { result ->
                when (result) {
                    is AppResult.Success -> {
                        updateState {
                            copy(
                                isLoading = false,
                                album = result.data,
                                error = null
                            )
                        }
                    }

                    is AppResult.Error -> {
                        updateState {
                            copy(
                                isLoading = false,
                                error = result.error.asUiText()
                            )
                        }
                    }
                }
            }
        }
    }
}