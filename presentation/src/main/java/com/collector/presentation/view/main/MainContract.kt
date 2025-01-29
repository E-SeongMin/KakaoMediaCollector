package com.collector.presentation.view.main

import com.collector.domain.kakao.model.SavedSearchEntity
import com.collector.domain.kakao.model.SearchResultEntity
import com.collector.presentation.view.base.BaseContract.*
import com.collector.presentation.view.search.SearchContract.Effect

interface MainContract {

    sealed class Event: UiEvent {
    }

    data class State(
        val saveList: List<SavedSearchEntity> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ) : UiState



    sealed class Effect: UiEffect {
        data class ShowError(val message: String) : Effect()
    }
}