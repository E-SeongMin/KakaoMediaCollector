package com.collector.data.di

import android.content.Context
import androidx.room.Room
import com.collector.data.kakao.datasource.local.dto.SavedSearchItemDao
import com.collector.data.kakao.datasource.remote.api.KakaoApiService
import com.collector.data.room.AppDatabase
import com.collector.data.room.DatabaseProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatabaseProvider(appDatabase: AppDatabase): DatabaseProvider {
        return DatabaseProvider(appDatabase)
    }
}