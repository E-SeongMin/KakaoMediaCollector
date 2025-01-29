package com.collector.data.di

import com.collector.data.kakao.KakaoConst
import com.google.gson.GsonBuilder
import com.collector.data.log.CustomLog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor { message ->
        CustomLog.d(message)
    }.also {
        it.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        cache: okhttp3.Cache
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor { chain ->
            val response = chain.proceed(chain.request())
            response.newBuilder()
                .header("Cache-Control", "public, max-age=300")
                .build()
        }
        .cache(cache)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(25, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideCache(): okhttp3.Cache {
        val cacheSize = 10L * 1024 * 1024
        val cacheDir = File(System.getProperty("java.io.tmpdir"), "http_cache")
        return okhttp3.Cache(cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(KakaoConst.KAKAO_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
}