package com.example.flashfeed.Profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryItem(
    val name: String,
    val icon: ImageVector
)

class CategoryViewModel : ViewModel() {

    private val categoryItems: List<CategoryItem> = listOf(
        CategoryItem("Trending", Icons.Filled.TrendingUp),
        CategoryItem("National", Icons.Filled.Flag),
        CategoryItem("Business", Icons.Filled.CurrencyRupee),
        CategoryItem("Sports", Icons.Filled.SportsSoccer),
        CategoryItem("World", Icons.Filled.Public),
        CategoryItem("Politics", Icons.Filled.AccountBalance),
        CategoryItem("Tech", Icons.Filled.Memory),
        CategoryItem("Startup", Icons.Filled.Lightbulb),
        CategoryItem("Ent", Icons.Filled.Movie),
        CategoryItem("Misc", Icons.Filled.Dashboard),
        CategoryItem("Hatke", Icons.Filled.AutoAwesome),
        CategoryItem("Science", Icons.Filled.Science),
        CategoryItem("Auto", Icons.Filled.DirectionsCar)
    )

    // Tracks selection state
    var categorySelection = mutableStateMapOf<String, Boolean>()
        private set

    init {
        val defaultSelected = listOf("Trending", "Business", "Tech")
        if (categorySelection.isEmpty()) {
            categoryItems.forEach { item ->
                categorySelection[item.name] = item.name in defaultSelected
            }
        }
    }

    // Toggle category selection with a max limit of 5
    fun toggleCategory(category: String) {
        // Check if the category exists in categoryItems
        if (category !in categoryItems.map { it.name }) return

        val currentSelectedCount = categorySelection.count { it.value }
        val isSelected = categorySelection[category] == true

        when {
            isSelected -> {
                categorySelection[category] = false
            }
            currentSelectedCount < 5 -> {
                categorySelection[category] = true
            }
        }
    }

    // Return selected CategoryItems (with icons) instead of just names
    fun getSelectedCategories(): List<CategoryItem> {
        return categoryItems.filter { categorySelection[it.name] == true }
    }

    fun getAllCategories(): List<CategoryItem> = categoryItems
}