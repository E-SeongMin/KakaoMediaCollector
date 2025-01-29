package com.collector.data.kakao.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.collector.domain.kakao.model.enums.SearchResultType

@Entity(tableName = "saved_search_items")
data class SavedSearchItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val thumbnail: String,
    val dateTime: String,
    val type: SearchResultType
)