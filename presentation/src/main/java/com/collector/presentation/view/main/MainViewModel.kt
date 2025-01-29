package com.collector.presentation.view.main

import androidx.lifecycle.viewModelScope
import com.collector.domain.kakao.usecase.DeleteSavedSearchItemUseCase
import com.collector.domain.kakao.usecase.GetSavedItemListUseCase
import com.collector.presentation.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSavedItemListUseCase: GetSavedItemListUseCase,
    private val deleteSavedSearchItemUseCase: DeleteSavedSearchItemUseCase
): BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    override fun createInitialState(): MainContract.State = MainContract.State()

    override fun handleEvent(event: MainContract.Event) {

    }

    init {
        setInitData(this)
    }

    fun setInitData(viewModel: MainViewModel) {
        viewModel.viewModelScope.launch {
            try {
                viewModel.refreshSavedListView()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteSavedSearchItem(id: Long) {
        viewModelScope.launch {
            try {
                deleteSavedSearchItemUseCase.invoke(id)
                refreshSavedListView()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun refreshSavedListView() {
        val result = getSavedItemListUseCase.invoke()
        setState { copy(saveList = result, isLoading = false) }
    }
}