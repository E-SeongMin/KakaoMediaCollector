package com.collector.data.kakao.repository

import com.collector.data.kakao.datasource.remote.RemoteDataSource
import com.collector.data.log.CustomLog
import com.collector.domain.kakao.model.SearchResultEntity
import com.collector.domain.kakao.model.enums.SearchResultType
import com.collector.domain.kakao.repository.RemoteRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : RemoteRepository {
    override suspend fun searchImageAndVideo(query: String, sort: String, page: Int, size: Int): List<SearchResultEntity> {
        return coroutineScope {
            try {
                val imageDeferred = async { remoteDataSource.searchImage(query, sort, page, size) }
                val videoDeferred = async { remoteDataSource.searchVideo(query, sort, page, size) }

                val imageList = imageDeferred.await().documents.map { item ->
                    SearchResultEntity(
                        name = item.display_sitename,
                        thumbnail = item.thumbnail_url,
                        dateTime = item.datetime,
                        type = SearchResultType.IMAGE
                    )
                }
                val videoList = videoDeferred.await().documents.map { item ->
                    SearchResultEntity(
                        name = item.title,
                        thumbnail = item.thumbnail,
                        dateTime = item.datetime,
                        type = SearchResultType.VIDEO
                    )
                }

                imageList.plus(videoList).sortedByDescending { it.dateTime }

            } catch (e: Exception) {
                e.printStackTrace()
                mutableListOf()
            }
        }
    }
}