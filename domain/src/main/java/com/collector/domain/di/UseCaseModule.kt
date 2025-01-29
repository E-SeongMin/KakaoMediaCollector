package com.collector.domain.di

import com.collector.domain.kakao.repository.LocalRepository
import com.collector.domain.kakao.repository.RemoteRepository
import com.collector.domain.kakao.usecase.DeleteSavedSearchItemUseCase
import com.collector.domain.kakao.usecase.GetSavedItemListUseCase
import com.collector.domain.kakao.usecase.KakaoSearchUseCase
import com.collector.domain.kakao.usecase.SaveSearchItemUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideKakaoSearchUseCase(remoteRepository: RemoteRepository, localRepository: LocalRepository): KakaoSearchUseCase {
        return KakaoSearchUseCase(remoteRepository, localRepository)
    }
    @Provides
    @Singleton
    fun provideSaveSearchItemUseCase(repository: LocalRepository): SaveSearchItemUseCase {
        return SaveSearchItemUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteSearchItemUseCase(repository: LocalRepository): DeleteSavedSearchItemUseCase {
        return DeleteSavedSearchItemUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetSearchItemListUseCase(repository: LocalRepository): GetSavedItemListUseCase {
        return GetSavedItemListUseCase(repository)
    }
}