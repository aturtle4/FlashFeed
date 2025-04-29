package com.example.flashfeed.Profile

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flashfeed.database.CategoryEntity
import com.example.flashfeed.database.FlashFeedDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class CategoryItem (
    val name: String,
    val icon: ImageVector
)

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    // Category definitions
    private val categoryItems: List<CategoryItem> = listOf(
        CategoryItem("National", Icons.Filled.Flag),
        CategoryItem("Business", Icons.Filled.CurrencyRupee),
        CategoryItem("Sports", Icons.Filled.SportsSoccer),
        CategoryItem("World", Icons.Filled.Public),
        CategoryItem("Politics", Icons.Filled.AccountBalance),
        CategoryItem("Technology", Icons.Filled.Memory),
        CategoryItem("Startup", Icons.Filled.Lightbulb),
        CategoryItem("Entrepreneurship", Icons.Filled.Movie),
        CategoryItem("Miscellaneous", Icons.Filled.Dashboard),
        CategoryItem("Science", Icons.Filled.Science),
        CategoryItem("Automobile", Icons.Filled.DirectionsCar)
    )

    // Tracks selection state
    val categorySelection = mutableStateMapOf<String, Boolean>()

    private val database = FlashFeedDatabase.getDatabase(application)
    private val categoryDao = database.categoryDao()

    init {
        viewModelScope.launch {
            try {
                val savedCategories = categoryDao.getAllCategories().first()

                if (savedCategories.isEmpty()) {
                    saveDefaultCategories()
                } else {
                    savedCategories.forEach { entity ->
                        categorySelection[entity.name] = entity.isSelected
                    }
                }
            } catch (e: Exception) {
                saveDefaultCategories()
            }
        }
    }

    private fun saveDefaultCategories() {
        val defaultSelected = listOf("National", "Business", "Technology")

        viewModelScope.launch {
            categoryItems.forEach { item ->
                val isSelected = item.name in defaultSelected
                categorySelection[item.name] = isSelected

                // Save to database
                val entity = CategoryEntity(name = item.name, isSelected = isSelected)
                categoryDao.insertCategory(entity)
            }
        }
    }

    fun toggleCategory(category: String) {
        // Check if the category exists
        if (category !in categoryItems.map { it.name }) return

        val currentSelectedCount = categorySelection.count { it.value }
        val isSelected = categorySelection[category] == true

        when {
            isSelected -> {
                categorySelection[category] = false
                saveCategorySelection(category, false)
            }
            currentSelectedCount < 5 -> {
                categorySelection[category] = true
                saveCategorySelection(category, true)
            }
        }
    }

    private fun saveCategorySelection(name: String, isSelected: Boolean) {
        viewModelScope.launch {
            categoryDao.updateCategorySelection(name, isSelected)
            refreshCategorySelectionFromDb()
        }
    }

    private suspend fun refreshCategorySelectionFromDb() {
        val updatedCategories = categoryDao.getAllCategories().first()
        categorySelection.clear()
        updatedCategories.forEach { entity ->
            categorySelection[entity.name] = entity.isSelected
        }
    }


    fun getSelectedCategories(): List<CategoryItem> {
        return categoryItems.filter { categorySelection[it.name] == true }
    }

    fun getAllCategories(): List<CategoryItem> = categoryItems

    fun refresh() {
        viewModelScope.launch {
            refreshCategorySelectionFromDb()
        }
    }


    // Factory for creating this ViewModel with application context
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
                return CategoryViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}