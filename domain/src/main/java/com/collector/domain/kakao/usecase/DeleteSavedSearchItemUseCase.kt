package com.collector.domain.kakao.usecase

import com.collector.domain.kakao.model.SavedSearchEntity
import com.collector.domain.kakao.model.SearchResultEntity
import com.collector.domain.kakao.repository.LocalRepository
import com.collector.domain.kakao.repository.RemoteRepository
import javax.inject.Inject


class DeleteSavedSearchItemUseCase @Inject constructor(private val repository: LocalRepository) {
    suspend operator fun invoke(id: Long) {
        return repository.delete(id)
    }
}