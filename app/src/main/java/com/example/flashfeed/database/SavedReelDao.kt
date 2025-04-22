package com.example.flashfeed.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedReelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedReel(reel: SavedReelEntity)

    @Query("DELETE FROM saved_reels WHERE id = :reelId")
    suspend fun removeSavedReel(reelId: String)

    @Query("SELECT * FROM saved_reels ORDER BY savedTimestamp DESC")
    fun getAllSavedReels(): Flow<List<SavedReelEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_reels WHERE id = :reelId LIMIT 1)")
    suspend fun isReelSaved(reelId: String): Boolean
}