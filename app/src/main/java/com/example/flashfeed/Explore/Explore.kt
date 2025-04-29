package com.example.flashfeed.Explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flashfeed.Profile.AccountInfo
import com.example.flashfeed.Profile.ArticleCard
import com.example.flashfeed.Profile.CategoryViewModel
import com.example.flashfeed.Profile.NewsReelViewModel
import com.example.flashfeed.reel_mechanism.NewsReelScreen

@Composable
fun Explore(
    categoryViewModel: CategoryViewModel,
    newsReelViewModel: NewsReelViewModel,
    accountInfo: AccountInfo?
) {
    var launchNews by remember { mutableStateOf(false) }
    var catLaunchNews by remember { mutableStateOf(Pair(false, "Trending")) }
    var selectedIndex by remember { mutableStateOf(0) } //  Added
    var currentPage by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    val newsList by remember { derivedStateOf { newsReelViewModel.newsList } }
    val language = accountInfo?.lang
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex == gridState.layoutInfo.totalItemsCount - 1 && !isLoading) {
                    isLoading = true
                    currentPage++
                    newsReelViewModel.fetchNews("Trending", currentPage * 12, language.toString(), false)
                    isLoading = false
                }
            }
    }

    if (launchNews) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            NewsReelScreen(
                newsList = newsList,
                viewModel = newsReelViewModel,
                category = "Trending",
                startIndex = selectedIndex,
                accountInfo = accountInfo // 🆕 Pass selected index
            )
            FloatingActionButton(
                onClick = { launchNews = false },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    } else if (catLaunchNews.first) {
        val filteredNews = newsList.filter { it.category == catLaunchNews.second }
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            NewsReelScreen(
                newsList = filteredNews,
                viewModel = newsReelViewModel,
                category = catLaunchNews.second,
                startIndex = selectedIndex,
                accountInfo = accountInfo // 🆕 Pass selected index here too if needed
            )
            FloatingActionButton(
                onClick = { catLaunchNews = Pair(false, catLaunchNews.second) },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Explore", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold), modifier = Modifier.padding(top = 16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = gridState,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(newsList) { article ->
                    val index = newsList.indexOf(article)
                    ArticleCard(article, onClick = {
                        selectedIndex = index // 🆕 Track index
                        launchNews = true
                    })
                }
            }
        }
    }
}

