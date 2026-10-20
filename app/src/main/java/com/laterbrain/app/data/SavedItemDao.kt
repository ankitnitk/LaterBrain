package com.laterbrain.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDao {

    @Query("SELECT * FROM saved_items WHERE status IN ('WAITING', 'SHOWN', 'SNOOZED') ORDER BY createdAt DESC")
    fun observeActive(): Flow<List<SavedItem>>

    @Query("SELECT * FROM saved_items WHERE status IN ('DONE', 'DISMISSED') ORDER BY createdAt DESC")
    fun observeFinished(): Flow<List<SavedItem>>

    @Query("SELECT * FROM saved_items WHERE id = :id")
    fun observe(id: Long): Flow<SavedItem?>

    @Query("SELECT * FROM saved_items WHERE id = :id")
    suspend fun get(id: Long): SavedItem?

    @Insert
    suspend fun insert(item: SavedItem): Long

    @Update
    suspend fun update(item: SavedItem)

    @Query("DELETE FROM saved_items WHERE id = :id")
    suspend fun delete(id: Long)
}
