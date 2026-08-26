package com.apps.album_detail

import com.apps.album.model.Album
import com.apps.album.repository.AlbumRepository
import com.apps.album.usecase.ObserveAlbumUseCase
import com.apps.album.usecase.ToggleFavoritesUseCase
import com.apps.analytics.AnalyticsEvent
import com.apps.analytics.AnalyticsScreen
import com.apps.analytics.AnalyticsTracker
import com.apps.common.result.AppResult
import com.apps.logger.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * Unit test suite for [AlbumDetailViewModel].
 * Tests album detail data loading and toggling favorite state.
 */
class AlbumDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val sampleAlbum = Album(
        id = 42,
        albumId = 3,
        title = "Detail Album Test",
        url = "https://placehold.co/600",
        thumbnailUrl = "https://placehold.co/150",
        isFavorite = true
    )

    private val fakeRepository = object : AlbumRepository {
        var toggledId: Int? = null

        override fun observeAlbums(limit: Int, offset: Int): Flow<AppResult<List<Album>, com.apps.album.error.AlbumError>> =
            flowOf(AppResult.Success(listOf(sampleAlbum)))

        override fun observeAlbum(id: Int): Flow<AppResult<Album?, com.apps.album.error.AlbumError>> =
            flowOf(AppResult.Success(if (id == 42) sampleAlbum else null))

        override suspend fun refreshAlbums(): AppResult<Unit, com.apps.album.error.AlbumError> =
            AppResult.Success(Unit)

        override suspend fun toggleFavorite(id: Int) {
            toggledId = id
        }

        override fun observeFavorite(id: Int): Flow<Boolean> = flowOf(id == 42)
    }

    private val fakeLogger = object : AppLogger {
        override fun debug(message: String) {}
        override fun info(message: String) {}
        override fun warning(message: String) {}
        override fun error(message: String, throwable: Throwable?) {}
    }

    private val fakeAnalyticsTracker = object : AnalyticsTracker {
        override fun trackScreen(screen: AnalyticsScreen) {}
        override fun trackEvent(event: AnalyticsEvent) {}
    }

    /**
     * Sets up main coroutine dispatcher for unit testing.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Resets main coroutine dispatcher after testing.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Verifies that emitting a Load intent updates UI state with the requested album details.
     */
    @Test
    fun loadAlbum_updatesUiStateWithAlbum() = runTest {
        val observeAlbumUseCase = ObserveAlbumUseCase(fakeRepository)
        val toggleFavoritesUseCase = ToggleFavoritesUseCase(fakeRepository)

        val viewModel = AlbumDetailViewModel(
            observeAlbum = observeAlbumUseCase,
            toggleFavorite = toggleFavoritesUseCase,
            logger = fakeLogger,
            analyticsTracker = fakeAnalyticsTracker
        )

        viewModel.onIntent(AlbumDetailIntent.Load(42))
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.state.value.album)
        assertEquals(42, viewModel.state.value.album?.id)
        assertEquals("Detail Album Test", viewModel.state.value.album?.title)
    }

    /**
     * Verifies that emitting a ToggleFavorite intent invokes the repository toggle function.
     */
    @Test
    fun toggleFavorite_callsRepositoryToggle() = runTest {
        val observeAlbumUseCase = ObserveAlbumUseCase(fakeRepository)
        val toggleFavoritesUseCase = ToggleFavoritesUseCase(fakeRepository)

        val vm = AlbumDetailViewModel(
            observeAlbum = observeAlbumUseCase,
            toggleFavorite = toggleFavoritesUseCase,
            logger = fakeLogger,
            analyticsTracker = fakeAnalyticsTracker
        )

        vm.onIntent(AlbumDetailIntent.Load(42))
        testDispatcher.scheduler.advanceUntilIdle()

        vm.onIntent(AlbumDetailIntent.ToggleFavorite)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(42, fakeRepository.toggledId)
    }
}
