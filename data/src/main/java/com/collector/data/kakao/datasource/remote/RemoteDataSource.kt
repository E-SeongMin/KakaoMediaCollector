package com.collector.data.kakao.datasource.remote

import com.collector.data.kakao.datasource.remote.api.KakaoApiService
import com.collector.domain.kakao.model.ImageEntity
import com.collector.domain.kakao.model.VideoEntity
import com.collector.domain.kakao.model.base.SearchResponseData
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val kakaoApiService : KakaoApiService
) {
    suspend fun searchImage(query: String, sort: String, page: Int, size: Int): SearchResponseData<ImageEntity> {
        return kakaoApiService.searchImage(query, sort, page, size)
    }

    suspend fun searchVideo(query: String, sort: String, page: Int, size: Int): SearchResponseData<VideoEntity> {
        return kakaoApiService.searchVideo(query, sort, page, size)
    }
}