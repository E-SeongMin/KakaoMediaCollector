package com.collector.data.kakao.datasource.remote.api

import com.collector.data.kakao.KakaoConst
import com.collector.domain.kakao.model.ImageEntity
import com.collector.domain.kakao.model.VideoEntity
import com.collector.domain.kakao.model.base.SearchResponseData
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoApiService {
    @Headers("Authorization: ${KakaoConst.KAKAO_AUTH_HEADER}")
    @GET("v2/search/image")
    suspend fun searchImage(
        @Query("query") query: String, // 검색을 원하는 질의어
        @Query("sort") sort: String, // 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
        @Query("page") page: Int, // 결과 페이지 번호, 1~50 사이의 값, 기본 값 1
        @Query("size") size: Int // 한 페이지에 보여질 문서 수, 1~80 사이의 값, 기본 값 80
    ): SearchResponseData<ImageEntity>

    @Headers("Authorization: ${KakaoConst.KAKAO_AUTH_HEADER}")
    @GET("v2/search/vclip")
    suspend fun searchVideo(
        @Query("query") query: String, // 검색을 원하는 질의어
        @Query("sort") sort: String, // 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
        @Query("page") page: Int, // 결과 페이지 번호, 1~50 사이의 값, 기본 값 1
        @Query("size") size: Int // 한 페이지에 보여질 문서 수, 1~80 사이의 값, 기본 값 80
    ): SearchResponseData<VideoEntity>
}