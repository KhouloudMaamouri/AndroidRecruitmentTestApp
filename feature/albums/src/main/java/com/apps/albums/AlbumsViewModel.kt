package com.apps.albums

import androidx.lifecycle.viewModelScope
import com.apps.album.usecase.ObserveAlbumsUseCase
import com.apps.album.usecase.RefreshAlbumsUseCase
import com.apps.analytics.AnalyticsEvent
import com.apps.analytics.AnalyticsTracker
import com.apps.common.result.AppResult
import com.apps.logger.AppLogger
import com.apps.mvi.MviViewModel
import com.apps.ui.mapper.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.apps.album.usecase.ToggleFavoritesUseCase

import kotlinx.coroutines.Job

/**
 * ViewModel managing UI state, intents, and side effects for the Albums screen.
 *
 * @property observeAlbums Use case for observing cached albums stream.
 * @property refreshAlbums Use case for triggering remote album data refresh.
 * @property toggleFavorite Use case for toggling album favorite state.
 * @property logger Application logger instance.
 * @property analyticsTracker Analytics tracker for logging user interaction events.
 */
@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val observeAlbums: ObserveAlbumsUseCase,
    private val refreshAlbums: RefreshAlbumsUseCase,
    private val toggleFavorite: ToggleFavoritesUseCase,
    private val logger: AppLogger,
    private val analyticsTracker: AnalyticsTracker,
) : MviViewModel<AlbumsIntent, AlbumsUiState>(initialState = AlbumsUiState()) {

    private val _effects = MutableSharedFlow<AlbumsEffect>()
    /** SharedFlow emitting one-off side effects like navigation or error toasts. */
    val effects: SharedFlow<AlbumsEffect> = _effects.asSharedFlow()

    private var currentLimit = DEFAULT_PAGE_SIZE
    private var observeJob: Job? = null

    init {
        observeAlbums()
        refresh()
    }

    /**
     * Processes user intents emitted on the Albums screen.
     *
     * @param intent The [AlbumsIntent] to process.
     */
    override fun handleIntent(intent: AlbumsIntent) {
        when (intent) {
            AlbumsIntent.Load -> {
                if (state.value.albums.isEmpty()) {
                    currentLimit = DEFAULT_PAGE_SIZE
                    observeAlbums()
                }
            }
            AlbumsIntent.LoadMore -> loadMoreAlbums()
            AlbumsIntent.Retry -> {
                currentLimit = DEFAULT_PAGE_SIZE
                observeAlbums()
                refresh()
            }

            is AlbumsIntent.AlbumClicked -> {
                viewModelScope.launch {

                    analyticsTracker.trackEvent(
                        AnalyticsEvent.AlbumSelected(
                            albumId = intent.album.id
                        )
                    )

                    _effects.emit(
                        AlbumsEffect.NavigateToAlbum(
                            albumId = intent.album.id,
                        )
                    )
                }
            }

            is AlbumsIntent.ToggleFavorite -> {
                viewModelScope.launch {
                    toggleFavorite(intent.id)
                }
            }
        }
    }

    private fun loadMoreAlbums() {
        if (state.value.isLoading || state.value.isLoadingMore || !state.value.canLoadMore) return

        updateState { copy(isLoadingMore = true) }
        currentLimit += DEFAULT_PAGE_SIZE
        observeAlbums()
    }

    private fun observeAlbums() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            observeAlbums.invoke(limit = currentLimit, offset = 0)
                .collect { result ->
                    when (result) {
                        is AppResult.Success -> {
                            updateState {
                                copy(
                                    albums = result.data,
                                    isLoadingMore = false,
                                    canLoadMore = result.data.size >= currentLimit,
                                    error = null
                                )
                            }
                        }

                        is AppResult.Error -> {
                            updateState {
                                copy(
                                    isLoadingMore = false,
                                    error = result.error.asUiText()
                                )
                            }
                        }
                    }
                }
        }
    }

    /**
     * Triggers a remote refresh operation to fetch latest album data.
     */
    fun refresh() {
        logger.info("Refreshing albums")

        viewModelScope.launch {
            updateState {
                copy(
                    isLoading = state.value.albums.isEmpty(),
                    isRefreshing = true,
                    error = null
                )
            }

            when (val result = refreshAlbums.invoke()) {

                is AppResult.Success -> {
                    // Nothing to do.
                    // Room Flow emits the updated albums automatically.
                }

                is AppResult.Error -> {
                    updateState {
                        copy(
                            error = result.error.asUiText()
                        )
                    }
                }
            }

            updateState {
                copy(
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 10
    }
}