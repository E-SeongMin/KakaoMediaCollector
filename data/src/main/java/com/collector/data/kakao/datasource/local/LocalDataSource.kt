package com.collector.data.kakao.datasource.local

import com.collector.data.kakao.datasource.local.dto.SavedSearchItemDao
import com.collector.data.kakao.datasource.local.entity.SavedSearchItemEntity
import com.collector.data.room.DatabaseProvider
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val databaseProvider: DatabaseProvider
) {

    private val savedSearchItemDao: SavedSearchItemDao = databaseProvider.getDatabase().savedSearchItemDao()

    suspend fun insert(item: SavedSearchItemEntity) {
        savedSearchItemDao.insert(item)
    }

    suspend fun delete(id: Long) {
        savedSearchItemDao.delete(id)
    }

    suspend fun getAllItems(): List<SavedSearchItemEntity> {
        return savedSearchItemDao.getAllItems()
    }
}