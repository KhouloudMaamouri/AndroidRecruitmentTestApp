package com.apps.albums.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object (DAO) for executing database operations on album entities.
 */
@Dao
interface AlbumDao {

    /**
     * Observes cached album entities ordered by album ID and item ID with pagination support.
     *
     * @param limit Maximum number of records to return, defaults to 10.
     * @param offset Number of records to skip, defaults to 0.
     * @return A [Flow] emitting lists of cached [AlbumEntity].
     */
    @Query("SELECT * FROM albums ORDER BY albumId, id LIMIT :limit OFFSET :offset")
    fun observeAlbums(
        limit: Int = 10,
        offset: Int = 0
    ): Flow<List<AlbumEntity>>

    /**
     * Observes a single album entity by its unique ID.
     *
     * @param id The unique identifier of the album item.
     * @return A [Flow] emitting the cached [AlbumEntity] or null.
     */
    @Query("SELECT * FROM albums WHERE id = :id")
    fun observeAlbum(id: Int): Flow<AlbumEntity?>

    /**
     * Inserts or replaces a list of album entities in the database.
     *
     * @param albums List of [AlbumEntity] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(
        albums: List<AlbumEntity>
    )

    /**
     * Deletes all album records from the albums table.
     */
    @Query("DELETE FROM albums")
    suspend fun deleteAllAlbums()

    /**
     * Transaction replacing all current cached albums with a new list.
     *
     * @param albums The new list of [AlbumEntity] records.
     */
    @Transaction
    suspend fun replaceAlbums(albums: List<AlbumEntity>) {
        deleteAllAlbums()
        insertAlbums(albums)
    }
}