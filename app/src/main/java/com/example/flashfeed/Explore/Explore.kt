package com.example.flashfeed.Explore

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.flashfeed.Profile.ArticleCard
import com.example.flashfeed.Profile.CategoryItem
import com.example.flashfeed.Profile.CategoryViewModel
import com.example.flashfeed.Profile.NewsReelViewModel
import com.example.flashfeed.reel_mechanism.NewsArticle
import com.example.flashfeed.reel_mechanism.NewsReelScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Explore(categoryViewModel: CategoryViewModel, newsReelViewModel: NewsReelViewModel) {
    var launchNews by remember { mutableStateOf(false) }
    var catLaunchNews by remember { mutableStateOf(Pair(false, "Trending")) }
    val pageSize = 20
    var currentPage by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    val newsList by remember { derivedStateOf { newsReelViewModel.newsList } }

    val gridState = rememberLazyGridState()

    LaunchedEffect (gridState){
        Log.d("Explore", "LaunchedEffect triggered")
        snapshotFlow {gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index}

            .collect { lastVisibleItemIndex ->
                Log.d("Explore", "Last visible item index: $lastVisibleItemIndex")
                val totalItems = gridState.layoutInfo.totalItemsCount
                Log.d("Explore", "Total items: $totalItems")
                if (lastVisibleItemIndex == totalItems - 1 && !isLoading) {
                    Log.d("Explore", "Loading more items...")
                    isLoading = true
                    currentPage++
                    Log.d("Explore", "Current page: $currentPage")
                    newsReelViewModel.fetchNews("Trending", currentPage*12, false)
                    Log.d("Explore", "Fetched more items: ${newsList.size}")
                    isLoading = false
                }
            }
    }


    if (launchNews){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // NewsReelScreen as background
            NewsReelScreen(newsList = newsList, newsReelViewModel, "Trending")

            // Floating Action Button as back
            FloatingActionButton(
                onClick = { launchNews = false },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to Profile",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
    else if(catLaunchNews.first){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // NewsReelScreen as background
            val filteredNews = newsList.filter { it.category == catLaunchNews.second }
            NewsReelScreen(newsList = filteredNews, newsReelViewModel, catLaunchNews.second)

            // Floating Action Button as back
            FloatingActionButton(
                onClick = { catLaunchNews = Pair(false, catLaunchNews.second) },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to Profile",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Explore",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Popular This Week",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = gridState,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(newsList) { article ->
                    ArticleCard(article,  onClick = { launchNews = !launchNews })
                }
            }

        }
    }
}
