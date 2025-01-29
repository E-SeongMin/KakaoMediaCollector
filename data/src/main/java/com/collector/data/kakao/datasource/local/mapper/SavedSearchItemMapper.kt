package com.collector.data.kakao.datasource.local.mapper

import com.collector.data.kakao.datasource.local.entity.SavedSearchItemEntity
import com.collector.domain.kakao.model.SavedSearchEntity

// SavedSearchItem -> SavedSearchItemEntity
fun SavedSearchEntity.toEntity(): SavedSearchItemEntity {
    return SavedSearchItemEntity(
        name = this.name,
        thumbnail = this.thumbnail,
        dateTime = this.dateTime,
        type = this.type
    )
}

// SavedSearchItemEntity -> SavedSearchItem
fun SavedSearchItemEntity.toDomainModel(): SavedSearchEntity {
    return SavedSearchEntity(
        id = this.id,
        name = this.name,
        thumbnail = this.thumbnail,
        dateTime = this.dateTime,
        type = this.type
    )
}