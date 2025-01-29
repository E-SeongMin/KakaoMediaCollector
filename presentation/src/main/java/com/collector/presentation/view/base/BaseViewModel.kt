package com.collector.presentation.view.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.collector.data.log.CustomLog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.collector.presentation.view.base.BaseContract.*

abstract class BaseViewModel<Event : UiEvent, State : UiState, Effect : UiEffect> : ViewModel() {

    private val initState : State by lazy { createInitialState() }

    abstract fun createInitialState() : State

    val currentState: State
        get() = uiState.value

    /**
     * StateFlow 는 초기값을 가지고 있다는 점만 제외하면 기존의 LiveDate 와 크게 다르지 않다.
     * 초기값을 가져야하고 항상 최신 값을 필요로 하는 UiState 에 적절하다.
     */
    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initState)
    val uiState = _uiState.asStateFlow()

    /**
     * SharedFlow 는 발생하는 이벤트 구독자가 0 명일 수 도 있고 여러 명일 수 도 있다.
     * 만약 구독자가 한명도 없다면, 이벤트는 그대로 무시된다.
     * 이벤트를 처리해야하는 구독자가 존재하지 않는다면 무시될 필요가 있는 UiEvent 에 적절하다.
     */
    private val _uiEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    /**
     * Channel 은 각각의 이벤트가 오직 하나의 구독자에게만 전달된다. 만약 구독자가 없을 때
     * 이벤트가 발생했다면 채널 버퍼가 가득차자마자 구독자가 나타날때까지 일시중지된다. 따라서 이벤트가 무시되지 않는다.
     * Channel 은 Hot Stream 이기도 하고 방향이 변경되거나 UI 가 다시 표시 될 때 Side Effect 를 다시 표시할 필요가 없다.
     * 단순하게 SingleLiveEvent 동작을 복제하고 싶기 때문에 Channel 을 사용한다.
     */
    private val _uiEffect: Channel<Effect> = Channel()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {

        /**
         * Events 를 처리하기 위해 Event Flow 를 수집해야 한다. 이는 init 블럭에서 처리한다.
         */
        subscribeEvents()
    }

    /**
     * Set New UI Event
     */
    protected fun setState(reduce: State.() -> State) {
        CustomLog.d("")
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    /**
     * Set New UI Event
     */
    fun setEvent(event: Event) {
        val newEvent = event
        viewModelScope.launch { _uiEvent.emit(newEvent) }
    }

    /**
     * Set New UI Effect
     */
    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _uiEffect.send(effectValue) }
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            uiEvent.collect {
                handleEvent(it)
            }
        }
    }

    abstract fun handleEvent(event: Event)
}