package com.collector.domain.kakao.model

import com.collector.domain.kakao.model.enums.SearchResultType
import java.util.Date

data class SearchResultEntity(
    var id: Long = 0,
    val name: String,
    val thumbnail : String,
    val dateTime : String,
    val type: SearchResultType,
    var isSaved: Boolean = false
)
