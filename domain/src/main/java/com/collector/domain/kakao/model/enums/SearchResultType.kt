package com.collector.domain.kakao.model.enums

enum class SearchResultType {
    IMAGE,
    VIDEO;

    override fun toString(): String {
        return when (this) {
            IMAGE -> "이미지"
            VIDEO -> "비디오"
        }
    }
}