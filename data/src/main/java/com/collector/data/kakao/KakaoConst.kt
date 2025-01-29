package com.collector.data.kakao

import com.collector.data.BuildConfig

object KakaoConst {
    const val KAKAO_BASE_URL = "https://dapi.kakao.com"
    const val KAKAO_AUTH_HEADER = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
}