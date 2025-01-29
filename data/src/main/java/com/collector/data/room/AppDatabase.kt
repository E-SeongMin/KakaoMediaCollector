package com.collector.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.collector.data.kakao.datasource.local.dto.SavedSearchItemDao
import com.collector.data.kakao.datasource.local.entity.SavedSearchItemEntity


@Database(entities = [SavedSearchItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedSearchItemDao(): SavedSearchItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}