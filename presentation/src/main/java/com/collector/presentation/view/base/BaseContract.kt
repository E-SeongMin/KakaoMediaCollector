package com.collector.presentation.view.base

interface BaseContract {
    // 화면의 상태(State)
    interface UiState

    // 사용자 동작 또는 이벤트(Intent)
    interface UiEvent

    // 화면에서 발생하는 단발성 효과(Effect), 예: 네비게이션, 토스트 메시지
    interface UiEffect
}
