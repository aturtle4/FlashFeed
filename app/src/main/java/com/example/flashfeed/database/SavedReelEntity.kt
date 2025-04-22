package com.example.flashfeed.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flashfeed.reel_mechanism.NewsArticle

@Entity(tableName = "saved_reels")
data class SavedReelEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String,
    val source: String,
    val timestamp: String,
    val articleLink: String,
    val category: String,
    val savedTimestamp: Long = System.currentTimeMillis()
) {
    fun toNewsArticle(): NewsArticle {
        return NewsArticle(
            id = id.toInt(),
            title = title,
            content = content,
            imageUrl = imageUrl,
            source = source,
            timestamp = timestamp,
            articleLink = articleLink,
            category = category
        )
    }

    companion object {
        fun fromNewsArticle(article: NewsArticle): SavedReelEntity {
            return SavedReelEntity(
                id = article.id.toString(),
                title = article.title,
                content = article.content,
                imageUrl = article.imageUrl,
                source = article.source,
                timestamp = article.timestamp,
                articleLink = article.articleLink,
                category = article.category
            )
        }
    }
}