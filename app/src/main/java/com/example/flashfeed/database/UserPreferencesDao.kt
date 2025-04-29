package com.example.flashfeed.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    fun getUserPreferences(): Flow<UserPreferencesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPreferences(preferences: UserPreferencesEntity)

    @Query("UPDATE user_preferences SET username = :username WHERE id = 1")
    suspend fun updateUsername(username: String)

    @Query("UPDATE user_preferences SET userImageUri = :imageUri WHERE id = 1")
    suspend fun updateUserImage(imageUri: String?)

    @Query("UPDATE user_preferences SET language = :language WHERE id = 1")
    suspend fun updateLanguage(language: String)
}