package com.example.flashfeed.reel_mechanism
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateMapOf

class NewsReelViewModel : ViewModel() {
    // Map to store liked states (Key: Article ID, Value: Boolean)
    val likedNewsMap = mutableStateMapOf<String, Boolean>()
    private val savedNewsMap = mutableStateMapOf<String, NewsArticle>()

    // Function to toggle like state
    fun toggleLike(newsId: String) {
        likedNewsMap[newsId] = !(likedNewsMap[newsId] ?: false)
    }

    // Function to check if an article is liked
    fun isLiked(newsId: String): Boolean {
        return likedNewsMap[newsId] ?: false
    }

    //Save a news article
    fun saveArticle(article: NewsArticle) {
        savedNewsMap[article.id.toString()] = article
    }

    // Remove a saved article
    fun removeSavedArticle(articleId: String) {
        savedNewsMap.remove(articleId)
    }

    // Check if an article is saved
    fun isArticleSaved(articleId: String): Boolean {
        return savedNewsMap.containsKey(articleId)
    }

    // Get all saved articles
    fun getSavedArticles(): List<NewsArticle> {
        return savedNewsMap.values.toList()
    }
}


