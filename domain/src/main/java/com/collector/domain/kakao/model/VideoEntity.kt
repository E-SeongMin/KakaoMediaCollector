package com.collector.domain.kakao.model

import java.util.Date

data class VideoEntity(
    val title : String,   // 동영상 제목
    val url : String,   // 동영상 링크
    val datetime : String,   // 동영상 등록일, ISO 8601
    val play_time : Int,   // 동영상 재생시간, 초 단위
    val thumbnail : String,   // 동영상 미리보기 URL
    val author : String   // 동영상 업로더
)
