package com.collector.data.di

import com.collector.data.kakao.datasource.remote.api.KakaoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Provides
    @Singleton
    fun provideKakaoApiService(retrofit: Retrofit): KakaoApiService {
        return retrofit.create(KakaoApiService::class.java)
    }
}