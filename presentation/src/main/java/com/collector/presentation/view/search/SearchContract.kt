package com.collector.presentation.view.search

import com.collector.domain.kakao.model.SearchResultEntity
import com.collector.presentation.view.base.BaseContract.*
import com.collector.presentation.view.main.MainContract.Effect

interface SearchContract {

    sealed class Event: UiEvent {
        data class SetSearchQuery(val query: String) : Event()
        object onClickSearchButton : Event()
    }

    data class State(
        val currentQuery: String = "",
        var previousQuery: String = "",
        val searchList: List<SearchResultEntity> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val isSearchSuccess: Boolean = false
    ) : UiState



    sealed class Effect: UiEffect {
        data class ShowError(val message: String) : Effect()
    }
}