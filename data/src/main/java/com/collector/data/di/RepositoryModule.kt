package com.collector.data.di

import com.collector.data.kakao.datasource.local.LocalDataSource
import com.collector.data.kakao.datasource.remote.RemoteDataSource
import com.collector.data.kakao.repository.LocalRepositoryImpl
import com.collector.data.kakao.repository.RemoteRepositoryImpl
import com.collector.domain.kakao.repository.LocalRepository
import com.collector.domain.kakao.repository.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideLocalRepository(
        localDataSource: LocalDataSource
    ) : LocalRepository = LocalRepositoryImpl(localDataSource)

    @Singleton
    @Provides
    fun provideRemoteRepository(
        remoteDataSource: RemoteDataSource
    ) : RemoteRepository = RemoteRepositoryImpl(remoteDataSource)
}