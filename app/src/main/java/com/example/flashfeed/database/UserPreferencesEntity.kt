package com.example.flashfeed.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val id: Int = 1, // Single instance
    val username: String,
    val userImageUri: String?,
    val language: String
)