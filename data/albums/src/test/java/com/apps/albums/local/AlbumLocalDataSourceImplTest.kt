package com.apps.albums.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [AlbumLocalDataSourceImpl].
 */
class AlbumLocalDataSourceImplTest {

    private val sampleEntity = AlbumEntity(
        id = 1,
        albumId = 100,
        title = "Local Entity",
        url = "https://example.com/l.png",
        thumbnailUrl = "https://example.com/l_thumb.png"
    )

    private val fakeDao = object : AlbumDao {
        val albumsFlow = MutableStateFlow<List<AlbumEntity>>(listOf(sampleEntity))
        val replacedList = mutableListOf<AlbumEntity>()
        var lastLimit: Int? = null
        var lastOffset: Int? = null

        override fun observeAlbums(limit: Int, offset: Int): Flow<List<AlbumEntity>> {
            lastLimit = limit
            lastOffset = offset
            return albumsFlow
        }
        override fun observeAlbum(id: Int): Flow<AlbumEntity?> =
            flowOf(albumsFlow.value.find { it.id == id })

        override suspend fun insertAlbums(albums: List<AlbumEntity>) {}

        override suspend fun deleteAllAlbums() {
            albumsFlow.value = emptyList()
        }

        override suspend fun replaceAlbums(albums: List<AlbumEntity>) {
            replacedList.clear()
            replacedList.addAll(albums)
            albumsFlow.value = albums
        }
    }

    @Test
    fun observeAlbums_emitsDaoAlbumsWithDefaultPagination() = runTest {
        val dataSource = AlbumLocalDataSourceImpl(fakeDao)
        val albums = dataSource.observeAlbums().first()

        assertEquals(1, albums.size)
        assertEquals("Local Entity", albums.first().title)
        assertEquals(10, fakeDao.lastLimit)
        assertEquals(0, fakeDao.lastOffset)
    }

    @Test
    fun observeAlbums_forwardsCustomLimitAndOffsetToDao() = runTest {
        val dataSource = AlbumLocalDataSourceImpl(fakeDao)
        dataSource.observeAlbums(limit = 25, offset = 50).first()

        assertEquals(25, fakeDao.lastLimit)
        assertEquals(50, fakeDao.lastOffset)
    }

    @Test
    fun observeAlbum_emitsTargetEntity() = runTest {
        val dataSource = AlbumLocalDataSourceImpl(fakeDao)
        val album = dataSource.observeAlbum(1).first()

        assertEquals(1, album?.id)
        assertEquals(100, album?.albumId)
    }

    @Test
    fun saveAlbums_invokesDaoReplaceAlbums() = runTest {
        val dataSource = AlbumLocalDataSourceImpl(fakeDao)
        val newEntity = sampleEntity.copy(id = 2, title = "New Saved Entity")

        dataSource.saveAlbums(listOf(newEntity))

        assertEquals(1, fakeDao.replacedList.size)
        assertEquals(2, fakeDao.replacedList.first().id)
        assertEquals("New Saved Entity", fakeDao.replacedList.first().title)
    }
}
