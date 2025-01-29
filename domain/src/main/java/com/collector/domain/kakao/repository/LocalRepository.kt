package com.collector.domain.kakao.repository

import com.collector.domain.kakao.model.SavedSearchEntity

interface LocalRepository {
    suspend fun insert(item: SavedSearchEntity)
    suspend fun delete(id: Long)
    suspend fun getAllItems(): List<SavedSearchEntity>
}