package com.example.flashfeed.Profile

import android.R.bool
import android.app.Application
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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flashfeed.API.RetrofitInstance
import androidx.lifecycle.viewModelScope
import com.example.flashfeed.database.FlashFeedDatabase
import com.example.flashfeed.database.SavedReelEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class NewsReelViewModel(application: Application) : AndroidViewModel(application) {
    private var currentPage = 1
    private val pageSize = 10
    private var isFetchingMore = false

    private val database = FlashFeedDatabase.getDatabase(application)
    private val savedReelDao = database.savedReelDao()

    // Like status map
    val likedNewsMap = mutableStateMapOf<String, Boolean>()

    var newsList by mutableStateOf<List<NewsArticle>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)

    // Saved articles map (internal cache)
    private val savedNewsMap = mutableStateMapOf<String, Boolean>()

    // Backing flow for saved articles list
    private val _savedNewsFlow = MutableStateFlow<List<NewsArticle>>(emptyList())
    val savedNewsFlow: StateFlow<List<NewsArticle>> = _savedNewsFlow.asStateFlow()

    private val _likedNewsFlow = MutableStateFlow<List<NewsArticle>>(emptyList())
    val likedNewsFlow: StateFlow<List<NewsArticle>> = _likedNewsFlow.asStateFlow()

    init {
        viewModelScope.launch {
            savedReelDao.getAllSavedReels().collectLatest { savedReels ->
                _savedNewsFlow.value = savedReels.map { it.toNewsArticle() }
                // Update in-memory cache for UI operations
                savedReels.forEach { reelEntity ->
                    likedNewsMap[reelEntity.id] = true
                }
            }
        }
    }
    // Toggle like
    fun toggleLike(newsId: String) {
        viewModelScope.launch {
            val currentLiked = savedNewsMap[newsId] ?: false
            savedNewsMap[newsId] = !currentLiked

            // Get the corresponding article

        }
    }

    fun isLiked(newsId: String): Boolean {
        return savedNewsMap[newsId] == true
    }

    // Save an article
    fun saveArticle(article: NewsArticle) {
        viewModelScope.launch {
            // Save to database
            val savedReel = SavedReelEntity.fromNewsArticle(article)
            savedReelDao.insertSavedReel(savedReel)

            // Update in-memory cache for UI
            likedNewsMap[article.id.toString()] = true

            Log.d("NewsReelViewModel", "Saved article to database: ${article.id}")
        }
    }

    // Remove a saved article
    fun removeSavedArticle(articleId: String) {
        viewModelScope.launch {
            // Remove from database
            savedReelDao.removeSavedReel(articleId)

            // Update in-memory cache
            likedNewsMap[articleId] = false

            Log.d("NewsReelViewModel", "Removed article from database: $articleId")
        }
    }

    fun isArticleSaved(articleId: String): Boolean {
        // For immediate UI feedback, use the in-memory map
        return likedNewsMap[articleId] == true
    }

    fun getSavedArticles(): List<NewsArticle> {
        return _savedNewsFlow.value
    }

    fun fetchNews(category: String, count: Int = pageSize + 5, language: String = "English", initial: Boolean = true): List<NewsArticle> {
        if (isFetchingMore) return emptyList()
        isFetchingMore = true
        if (initial) isLoading = true // Only show loading for initial fetch
        var lang = ""
        if (language == "en")
        {
            lang = "English"
        }
        else if (language == "hi")
        {
            lang = "Hindi"
        }
        else if (language == "bn")
        {
            lang = "Bengali"
        }
        else if (language == "ur")
        {
            lang = "Urdu"
        }
        else
        {
            lang = language
        }

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getNewsByCategory(category, count, initial.toString(), lang)
                if (initial) {
                    newsList = response
                } else {
                    newsList = newsList + response
                    currentPage++
                }

                response.forEach { article ->
                    val isLiked = savedReelDao.isReelSaved(article.id.toString())
                    likedNewsMap[article.id.toString()] = isLiked
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isFetchingMore = false
                isLoading = false // Will be false even if it was never true, which is fine
            }
        }
        return newsList
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsReelViewModel::class.java)) {
                return NewsReelViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
