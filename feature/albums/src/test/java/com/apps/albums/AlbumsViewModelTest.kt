package com.apps.albums

import com.apps.album.error.AlbumError
import com.apps.album.model.Album
import com.apps.album.repository.AlbumRepository
import com.apps.album.usecase.ObserveAlbumsUseCase
import com.apps.album.usecase.RefreshAlbumsUseCase
import com.apps.album.usecase.ToggleFavoritesUseCase
import com.apps.analytics.AnalyticsEvent
import com.apps.analytics.AnalyticsScreen
import com.apps.analytics.AnalyticsTracker
import com.apps.common.result.AppResult
import com.apps.logger.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit test suite for [AlbumsViewModel] inside feature:albums module.
 */
class AlbumsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val sampleAlbum = Album(
        id = 1,
        albumId = 1,
        title = "Feature Test Album",
        url = "https://placehold.co/600",
        thumbnailUrl = "https://placehold.co/150",
        isFavorite = false
    )

    private val fakeRepository = object : AlbumRepository {
        val albumsFlow = MutableStateFlow<AppResult<List<Album>, AlbumError>>(
            AppResult.Success(listOf(sampleAlbum))
        )
        var refreshCalled = false
        var toggledId: Int? = null
        var lastLimitRequested: Int? = null

        override fun observeAlbums(limit: Int, offset: Int): Flow<AppResult<List<Album>, AlbumError>> {
            lastLimitRequested = limit
            return albumsFlow
        }

        override fun observeAlbum(id: Int): Flow<AppResult<Album?, AlbumError>> =
            flowOf(AppResult.Success(sampleAlbum))

        override suspend fun refreshAlbums(): AppResult<Unit, AlbumError> {
            refreshCalled = true
            return AppResult.Success(Unit)
        }

        override suspend fun toggleFavorite(id: Int) {
            toggledId = id
        }

        override fun observeFavorite(id: Int): Flow<Boolean> = flowOf(false)
    }

    private val fakeLogger = object : AppLogger {
        override fun debug(message: String) {}
        override fun info(message: String) {}
        override fun warning(message: String) {}
        override fun error(message: String, throwable: Throwable?) {}
    }

    private val fakeAnalyticsTracker = object : AnalyticsTracker {
        var trackedEvent: AnalyticsEvent? = null
        override fun trackScreen(screen: AnalyticsScreen) {}
        override fun trackEvent(event: AnalyticsEvent) {
            trackedEvent = event
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_observesAlbumsAndTriggersRefresh() = runTest {
        val observeUseCase = ObserveAlbumsUseCase(fakeRepository)
        val refreshUseCase = RefreshAlbumsUseCase(fakeRepository)
        val toggleUseCase = ToggleFavoritesUseCase(fakeRepository)

        val vm = AlbumsViewModel(
            observeAlbums = observeUseCase,
            refreshAlbums = refreshUseCase,
            toggleFavorite = toggleUseCase,
            logger = fakeLogger,
            analyticsTracker = fakeAnalyticsTracker
        )

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, vm.state.value.albums.size)
        assertEquals("Feature Test Album", vm.state.value.albums.first().title)
        assertTrue(fakeRepository.refreshCalled)
        assertFalse(vm.state.value.isRefreshing)
    }

    @Test
    fun albumClickedIntent_tracksAnalyticsAndEmitsEffect() = runTest {
        val observeUseCase = ObserveAlbumsUseCase(fakeRepository)
        val refreshUseCase = RefreshAlbumsUseCase(fakeRepository)
        val toggleUseCase = ToggleFavoritesUseCase(fakeRepository)

        val vm = AlbumsViewModel(
            observeAlbums = observeUseCase,
            refreshAlbums = refreshUseCase,
            toggleFavorite = toggleUseCase,
            logger = fakeLogger,
            analyticsTracker = fakeAnalyticsTracker
        )

        vm.onIntent(AlbumsIntent.AlbumClicked(sampleAlbum))
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(fakeAnalyticsTracker.trackedEvent)
        assertTrue(fakeAnalyticsTracker.trackedEvent is AnalyticsEvent.AlbumSelected)
        assertEquals(1, (fakeAnalyticsTracker.trackedEvent as AnalyticsEvent.AlbumSelected).albumId)
    }

    @Test
    fun toggleFavoriteIntent_invokesRepositoryToggle() = runTest {
        val observeUseCase = ObserveAlbumsUseCase(fakeRepository)
        val refreshUseCase = RefreshAlbumsUseCase(fakeRepository)
        val toggleUseCase = ToggleFavoritesUseCase(fakeRepository)

        val vm = AlbumsViewModel(
            observeAlbums = observeUseCase,
            refreshAlbums = refreshUseCase,
            toggleFavorite = toggleUseCase,
            logger = fakeLogger,
            analyticsTracker = fakeAnalyticsTracker
        )

        vm.onIntent(AlbumsIntent.ToggleFavorite(id = 5))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(5, fakeRepository.toggledId)
    }

    @Test
    fun refreshWithEmptyAlbums_resetsLoadingState() = runTest {
        val emptyRepository = object : AlbumRepository by fakeRepository {
            override fun observeAlbums(limit: Int, offset: Int): Flow<AppResult<List<Album>, AlbumError>> =
                flowOf(AppResult.Success(emptyList()))

            override suspend fun refreshAlbums(): AppResult<Unit, AlbumError> {
                return AppResult.Success(Unit)
            }
        }

        val observeUseCase = ObserveAlbumsUseCase(emptyRepository)
        val refreshUseCase = RefreshAlbumsUseCase(emptyRepository)
        val toggleUseCase = ToggleFavoritesUseCase(emptyRepository)

        val vm = AlbumsViewModel(
            observeAlbums = observeUseCase,
            refreshAlbums = refreshUseCase,
            toggleFavorite = toggleUseCase,
            logger = fakeLogger,
            analyticsTracker = fakeAnalyticsTracker
        )

        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(vm.state.value.isLoading)
        assertFalse(vm.state.value.isRefreshing)
    }

    @Test
    fun loadMoreIntent_increasesLimitAndRequestsMoreFromRepository() = runTest {
        val tenAlbums = List(10) { index ->
            sampleAlbum.copy(id = index + 1)
        }
        fakeRepository.albumsFlow.value = AppResult.Success(tenAlbums)

        val observeUseCase = ObserveAlbumsUseCase(fakeRepository)
        val refreshUseCase = RefreshAlbumsUseCase(fakeRepository)
        val toggleUseCase = ToggleFavoritesUseCase(fakeRepository)

        val vm = AlbumsViewModel(
            observeAlbums = observeUseCase,
            refreshAlbums = refreshUseCase,
            toggleFavorite = toggleUseCase,
            logger = fakeLogger,
            analyticsTracker = fakeAnalyticsTracker
        )

        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(10, fakeRepository.lastLimitRequested)
        assertTrue(vm.state.value.canLoadMore)

        vm.onIntent(AlbumsIntent.LoadMore)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(20, fakeRepository.lastLimitRequested)
    }

    @Test
    fun loadIntentWhenAlbumsAlreadyLoaded_preservesLimitAndLoadedAlbums() = runTest {
        val tenAlbums = List(10) { index ->
            sampleAlbum.copy(id = index + 1)
        }
        fakeRepository.albumsFlow.value = AppResult.Success(tenAlbums)

        val observeUseCase = ObserveAlbumsUseCase(fakeRepository)
        val refreshUseCase = RefreshAlbumsUseCase(fakeRepository)
        val toggleUseCase = ToggleFavoritesUseCase(fakeRepository)

        val vm = AlbumsViewModel(
            observeAlbums = observeUseCase,
            refreshAlbums = refreshUseCase,
            toggleFavorite = toggleUseCase,
            logger = fakeLogger,
            analyticsTracker = fakeAnalyticsTracker
        )

        testDispatcher.scheduler.advanceUntilIdle()

        vm.onIntent(AlbumsIntent.LoadMore)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(20, fakeRepository.lastLimitRequested)

        vm.onIntent(AlbumsIntent.Load)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(20, fakeRepository.lastLimitRequested)
    }
}
