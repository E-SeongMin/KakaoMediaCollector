package com.collector.presentation.view.search

import androidx.lifecycle.viewModelScope
import com.collector.domain.kakao.model.SavedSearchEntity
import com.collector.domain.kakao.usecase.KakaoSearchUseCase
import com.collector.presentation.util.extension.toResString
import com.collector.presentation.view.base.BaseViewModel
import com.collector.presentation.R
import com.collector.domain.kakao.usecase.DeleteSavedSearchItemUseCase
import com.collector.domain.kakao.usecase.SaveSearchItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val saveSearchItemUseCase: SaveSearchItemUseCase,
    private val deleteSavedSearchItemUseCase: DeleteSavedSearchItemUseCase,
    private val searchUseCase: KakaoSearchUseCase
): BaseViewModel<SearchContract.Event, SearchContract.State, SearchContract.Effect>() {

    override fun createInitialState(): SearchContract.State = SearchContract.State()

    override fun handleEvent(event: SearchContract.Event) {
        when (event) {
            is SearchContract.Event.SetSearchQuery -> {
                setState { copy(currentQuery = event.query) }
            }
            SearchContract.Event.onClickSearchButton -> {
                search()
            }
        }
    }

    init {
        setInitData(this)
    }

    private fun setInitData(viewModel: SearchViewModel) {
        viewModel.viewModelScope.launch {
            try {
                viewModel.refreshSearchListData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun search() {
        if (currentState.currentQuery.isBlank()) {
            setEffect { SearchContract.Effect.ShowError(String.format(R.string.search_activity_error_no_query.toResString)) }
            return
        }

        if (currentState.currentQuery == currentState.previousQuery) {
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val searchQuery = currentState.currentQuery
            val result = searchUseCase.invoke(searchQuery)

            setState { copy(searchList = result, previousQuery = searchQuery, currentQuery = "", isLoading = false, errorMessage = null) }
        }
    }

    fun saveSearchItem(item: SavedSearchEntity) {
        viewModelScope.launch {
            saveSearchItemUseCase.invoke(item)
            refreshSearchListData()
        }
    }

    fun deleteSavedSearchItem(id: Long) {
        viewModelScope.launch {
            deleteSavedSearchItemUseCase.invoke(id)
            refreshSearchListData()
        }
    }

    private suspend fun refreshSearchListData() {
        val result = searchUseCase.invoke(currentState.previousQuery)
        setState { copy(searchList = result, isLoading = false, isSearchSuccess = true) }
    }
}