package com.collector.domain.kakao.usecase

import com.collector.domain.kakao.model.SearchResultEntity
import com.collector.domain.kakao.repository.LocalRepository
import com.collector.domain.kakao.repository.RemoteRepository
import javax.inject.Inject


class KakaoSearchUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(query: String, sort: String = "recency", page: Int = 1, size: Int = 80): List<SearchResultEntity> {
        if (query.isBlank()) {
            throw IllegalArgumentException("query field must be filled out")
        }

        val searchResult = remoteRepository.searchImageAndVideo(query, sort, page, size)
        val savedResult = localRepository.getAllItems()

        if (savedResult.isNotEmpty()) {
            savedResult.forEach { savedItem ->
                searchResult.forEach { searchItem ->
                    if (savedItem.thumbnail == searchItem.thumbnail) {
                        searchItem.id = savedItem.id
                        searchItem.isSaved = true
                    }
                }
            }
        }

        return searchResult
    }
}