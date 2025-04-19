package com.example.flashfeed.Profile

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.flashfeed.reel_mechanism.NewsArticle
import androidx.compose.runtime.mutableStateMapOf

class NewsReelViewModel : ViewModel() {

    // Like status map
    val likedNewsMap = mutableStateMapOf<String, Boolean>()

    // Saved articles map (internal cache)
    private val savedNewsMap = mutableStateMapOf<String, NewsArticle>()

    // Backing flow for saved articles list
    private val _savedNewsFlow = MutableStateFlow<List<NewsArticle>>(emptyList())
    val savedNewsFlow: StateFlow<List<NewsArticle>> = _savedNewsFlow.asStateFlow()

    // Toggle like
    fun toggleLike(newsId: String) {
        likedNewsMap[newsId] = !(likedNewsMap[newsId] ?: false)
    }

    fun isLiked(newsId: String): Boolean {
        return likedNewsMap[newsId] ?: false
    }

    // Save an article
    fun saveArticle(article: NewsArticle) {
        savedNewsMap[article.id.toString()] = article
        _savedNewsFlow.value = savedNewsMap.values.toList()
        Log.d("newsreelviewmodel", "$savedNewsMap")
    }

    // Remove a saved article
    fun removeSavedArticle(articleId: String) {
        savedNewsMap.remove(articleId)
        _savedNewsFlow.value = savedNewsMap.values.toList()
    }

    fun isArticleSaved(articleId: String): Boolean {
        return savedNewsMap.containsKey(articleId)
    }

    fun getSavedArticles(): List<NewsArticle> {
        return _savedNewsFlow.value
    }
}
