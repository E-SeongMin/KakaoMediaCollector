package com.collector.data.room

import android.content.Context
import androidx.room.Room
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseProvider @Inject constructor(private val appDatabase: AppDatabase) {

    fun getDatabase(): AppDatabase {
        return appDatabase
    }

    companion object {
        // Factory method to create DatabaseProvider for testing or manual DI
        fun create(context: Context): DatabaseProvider {
            val appDatabase = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()

            return DatabaseProvider(appDatabase)
        }
    }
}