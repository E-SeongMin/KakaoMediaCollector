package com.collector.domain.kakao.repository

import com.collector.domain.kakao.model.SearchResultEntity

interface RemoteRepository {
    suspend fun searchImageAndVideo(query: String, sort: String, page: Int, size: Int): List<SearchResultEntity>
}