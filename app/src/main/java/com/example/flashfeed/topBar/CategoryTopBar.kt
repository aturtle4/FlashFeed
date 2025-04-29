package com.example.flashfeed.topBar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoryTopBar(
    categories: List<String>,
    selectedTab: Int,
    onCategorySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = selectedTab)
    val coroutineScope = rememberCoroutineScope()

    // Sync pager with the selectedTab from the parent
    LaunchedEffect(selectedTab) {
        if (pagerState.currentPage != selectedTab) {
            pagerState.animateScrollToPage(selectedTab)
        }
    }

    // Sync tab clicks & swipes
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != selectedTab) {
            onCategorySelected(pagerState.currentPage)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Transparent TabRow
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), // Maintain height, remove solid background
            containerColor = Color.Transparent, // Fully transparent
            contentColor = MaterialTheme.colorScheme.onSurface, // Text color
            divider = {}, // Remove bottom divider
            indicator = {} // Remove selection indicator
        ) {
            categories.forEachIndexed { index, category ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        coroutineScope.launch {
                            onCategorySelected(index) // Update parent state
                            pagerState.animateScrollToPage(index) // Move pager
                        }
                    },
                    text = {
                        Text(
                            category,
                            color = if (selectedTab == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) // Fade effect for unselected tabs
                        )
                    }
                )
            }
        }
    }
}
