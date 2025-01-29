package com.collector.domain.kakao.model

import java.util.Date

data class ImageEntity(
    val collection : String,   // 컬렉션
    val thumbnail_url : String,   // 미리보기 이미지 URL - 이걸 사용할 예정
    val image_url : String,   // 이미지 URL
    val width : Int,   // 이미지의 가로 길이
    val height : Int,   // 이미지의 세로 길이
    val display_sitename : String,    // 출처
    val doc_url : String,   // 문서 URL
    val datetime : String   // 문서 작성시간, ISO 8601
)
