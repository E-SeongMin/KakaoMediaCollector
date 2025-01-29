package com.collector.data.kakao.datasource.local.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.collector.data.kakao.datasource.local.entity.SavedSearchItemEntity

@Dao
interface SavedSearchItemDao {

    @Insert
    suspend fun insert(item: SavedSearchItemEntity)

    @Query("DELETE FROM saved_search_items WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM saved_search_items")
    suspend fun getAllItems(): List<SavedSearchItemEntity>

}