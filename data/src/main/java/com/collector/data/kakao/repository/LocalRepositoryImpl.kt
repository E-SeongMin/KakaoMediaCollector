package com.collector.data.kakao.repository

import com.collector.data.kakao.datasource.local.LocalDataSource
import com.collector.data.kakao.datasource.local.mapper.toDomainModel
import com.collector.data.kakao.datasource.local.mapper.toEntity
import com.collector.domain.kakao.model.SavedSearchEntity
import com.collector.domain.kakao.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : LocalRepository {

    override suspend fun insert(item: SavedSearchEntity) {
        val entity = item.toEntity()
        withContext(Dispatchers.IO) {
            localDataSource.insert(entity)
        }
    }

    override suspend fun delete(id: Long) {
        withContext(Dispatchers.IO) {
            localDataSource.delete(id)
        }
    }

    override suspend fun getAllItems(): List<SavedSearchEntity> {
        return withContext(Dispatchers.IO) {
            val entities = localDataSource.getAllItems()
            entities.map { it.toDomainModel() }
        }
    }
}
