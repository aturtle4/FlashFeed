package com.example.flashfeed.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SavedReelEntity::class, CategoryEntity::class, UserPreferencesEntity::class],
    version = 3,
    exportSchema = false
)
abstract class FlashFeedDatabase : RoomDatabase() {
    abstract fun savedReelDao(): SavedReelDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userPreferencesDao(): UserPreferencesDao

    companion object {
        @Volatile
        private var INSTANCE: FlashFeedDatabase? = null

        fun getDatabase(context: Context): FlashFeedDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlashFeedDatabase::class.java,
                    "flashfeed_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}