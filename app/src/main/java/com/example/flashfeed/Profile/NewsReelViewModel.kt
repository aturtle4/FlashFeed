package com.example.flashfeed.Profile

import android.R.bool
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.flashfeed.reel_mechanism.NewsArticle
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.flashfeed.API.RetrofitInstance
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class NewsReelViewModel : ViewModel() {
    private var currentPage = 1
    private val pageSize = 10
    private var isFetchingMore = false


    // Like status map
    val likedNewsMap = mutableStateMapOf<String, Boolean>()

    var newsList by mutableStateOf<List<NewsArticle>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)

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

    fun fetchNews(category: String, count: Int = pageSize, initial: Boolean = true) {
        if (isFetchingMore) return
        isFetchingMore = true
        if (initial) isLoading = true // Only show loading for initial fetch

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getNewsByCategory(category, count, initial.toString())
                if (initial) {
                    newsList = response
                } else {
                    newsList = newsList + response
                    currentPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isFetchingMore = false
                isLoading = false // Will be false even if it was never true, which is fine
            }
        }
    }


}
