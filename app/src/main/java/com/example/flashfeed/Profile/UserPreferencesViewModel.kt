package com.example.flashfeed.Profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flashfeed.database.FlashFeedDatabase
import com.example.flashfeed.database.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserPreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FlashFeedDatabase.getDatabase(application)
    private val userPreferencesDao = database.userPreferencesDao()

    val userPreferences = userPreferencesDao.getUserPreferences()

    fun saveUserPreferences(username: String, userImageUri: String?, language: String) {
        viewModelScope.launch {
            val preferences = UserPreferencesEntity(
                username = username,
                userImageUri = userImageUri,
                language = language
            )
            userPreferencesDao.insertUserPreferences(preferences)
        }
    }

    fun updateUsername(username: String) {
        viewModelScope.launch {
            userPreferencesDao.updateUsername(username)
        }
    }

    fun updateUserImage(uri: String?) {
        viewModelScope.launch {
            userPreferencesDao.updateUserImage(uri)
        }
    }

    fun updateLanguage(language: String) {
        viewModelScope.launch {
            userPreferencesDao.updateLanguage(language)
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserPreferencesViewModel::class.java)) {
                return UserPreferencesViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}