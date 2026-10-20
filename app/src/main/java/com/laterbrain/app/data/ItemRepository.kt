package com.laterbrain.app.data

import kotlinx.coroutines.flow.Flow

/** The single place the rest of the app goes to read or change saved items. */
class ItemRepository(private val dao: SavedItemDao) {

    val activeItems: Flow<List<SavedItem>> = dao.observeActive()
    val finishedItems: Flow<List<SavedItem>> = dao.observeFinished()

    fun observe(id: Long): Flow<SavedItem?> = dao.observe(id)

    suspend fun get(id: Long): SavedItem? = dao.get(id)

    /** Inserts a new item (id 0) or updates an existing one. Returns the item's id. */
    suspend fun save(item: SavedItem): Long =
        if (item.id == 0L) dao.insert(item) else item.id.also { dao.update(item) }

    suspend fun setStatus(id: Long, status: ItemStatus) {
        val item = dao.get(id) ?: return
        dao.update(item.copy(status = status))
    }

    suspend fun delete(id: Long) = dao.delete(id)
}
