package com.collector.domain.kakao.model

import com.collector.domain.kakao.model.enums.SearchResultType

data class SavedSearchEntity(
    val id: Long = 0,
    val name: String,
    val thumbnail: String,
    val dateTime: String,
    val type: SearchResultType
)