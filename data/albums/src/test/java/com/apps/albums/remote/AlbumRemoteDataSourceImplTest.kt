package com.apps.albums.remote

import com.apps.util.NetworkResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

/**
 * Unit tests for [AlbumRemoteDataSourceImpl].
 */
class AlbumRemoteDataSourceImplTest {

    private val sampleDto = AlbumDto(
        id = 1,
        albumId = 5,
        title = "Remote Dto",
        url = "https://example.com/r.png",
        thumbnailUrl = "https://example.com/r_thumb.png"
    )

    private val fakeApiSuccess = object : AlbumApi {
        override suspend fun getAlbums(): List<AlbumDto> = listOf(sampleDto)
    }

    private val fakeApiError = object : AlbumApi {
        override suspend fun getAlbums(): List<AlbumDto> {
            throw IOException("Network error")
        }
    }

    @Test
    fun getAlbums_success_returnsSuccessNetworkResult() = runTest {
        val dataSource = AlbumRemoteDataSourceImpl(fakeApiSuccess)
        val result = dataSource.getAlbums()

        assertTrue(result is NetworkResult.Success)
        val list = (result as NetworkResult.Success).data
        assertEquals(1, list.size)
        assertEquals("Remote Dto", list.first().title)
    }

    @Test
    fun getAlbums_failure_returnsErrorNetworkResult() = runTest {
        val dataSource = AlbumRemoteDataSourceImpl(fakeApiError)
        val result = dataSource.getAlbums()

        assertTrue(result is NetworkResult.Error)
    }
}
