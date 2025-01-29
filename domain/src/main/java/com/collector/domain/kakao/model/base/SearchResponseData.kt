package com.collector.domain.kakao.model.base

data class SearchResponseData<T> (
    val metaData : MetaData,
    var documents : MutableList<T>
)