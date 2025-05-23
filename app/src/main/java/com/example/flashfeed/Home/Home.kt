package com.example.flashfeed.Home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashfeed.Profile.AccountInfo
import com.example.flashfeed.Profile.CategoryViewModel
import com.example.flashfeed.Profile.CategoryItem
import com.example.flashfeed.Profile.NewsReelViewModel
import com.example.flashfeed.reel_mechanism.NewsReelScreen
import com.example.flashfeed.R

@Composable
fun Home(
    categoryViewModel: CategoryViewModel,
    newsReelViewModel: NewsReelViewModel,
    accountInfo: AccountInfo?
) {
    LaunchedEffect(Unit) {
        categoryViewModel.refresh()
    }
    val selectedCategories = categoryViewModel.getSelectedCategories()
    var selectedTab by remember { mutableStateOf(selectedCategories.firstOrNull()?.name ?: "Trending") }

    Box(modifier = Modifier.fillMaxSize()) {
        GetRespNews(category = selectedTab, newsReelViewModel, accountInfo)
        TopBar(
            categories = selectedCategories,
            selectedTab = selectedTab,
            onTabSelected = { categoryName ->
                selectedTab = categoryName
            }
        )
    }
}


@Composable
fun TopBar(
    categories: List<CategoryItem>,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val context = LocalContext.current
        categories.forEach { category ->
            val isSelected = category.name == selectedTab
            val languageMap = {
                mapOf(
                    "National" to context.getString(R.string.national),
                    "Business" to context.getString(R.string.business),
                    "Sports" to context.getString(R.string.sports),
                    "World" to context.getString(R.string.world),
                    "Politics" to context.getString(R.string.politics),
                    "Technology" to context.getString(R.string.technology),
                    "Startup" to context.getString(R.string.startup),
                    "Entrepreneurship" to context.getString(R.string.entrepreneurship),
                    "Miscellaneous" to context.getString(R.string.miscellaneous),
                    "Science" to context.getString(R.string.science),
                    "Automobile" to context.getString(R.string.automobile)
                )
            }

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onTabSelected(category.name) }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    modifier = Modifier.size(20.dp),
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = languageMap()[category.name] ?: category.name,
                    fontSize = 12.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GetRespNews(category: String, viewModel: NewsReelViewModel, accountInfo: AccountInfo?) {
    val language = accountInfo?.lang
    Log.d("language", language.toString())
    LaunchedEffect(category) {
        println(category)
        viewModel.fetchNews(category, 10, language.toString())
    }

    if (viewModel.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        NewsReelScreen(newsList = viewModel.newsList, viewModel, category, accountInfo =  accountInfo)
    }
}

