package com.example.flashfeed.Profile

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flashfeed.reel_mechanism.NewsArticle
import com.example.flashfeed.Profile.NewsReelViewModel
import com.example.flashfeed.reel_mechanism.NewsReelScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(viewModel: CategoryViewModel, newsReelViewModel: NewsReelViewModel) {
    var drawerOpen by remember { mutableStateOf(false) }
    val inverseOnSurface = MaterialTheme.colorScheme.onSurface.run {
        Color(1f - red, 1f - green, 1f - blue, alpha)
    }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val savedCardHeightCollapsed = 180.dp
    val savedCardHeightExpanded = 500.dp
    var openSavedNews by remember { mutableStateOf(false) }
    val news by newsReelViewModel.savedNewsFlow.collectAsState()
    Log.d("openSavedNEWs", "openSavedNews : $openSavedNews")
    if (openSavedNews) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // NewsReelScreen as background
            NewsReelScreen(newsList = news, newsReelViewModel)

            // Floating Action Button as back
            FloatingActionButton(
                onClick = { openSavedNews = false },
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
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Main Content
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                ProfileHeader(userName = "aTurtle4")
                Spacer(modifier = Modifier.height(55.dp))

                SavedArticles(news){openSavedNews = true}
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(onClick = { drawerOpen = !drawerOpen }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        modifier = Modifier.size(36.dp),
                        tint = inverseOnSurface,
                        contentDescription = "Settings-Menu"
                    )
                }
            }
        }
    }
        if (drawerOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { drawerOpen = false })
                    }
            )
        }

        if (drawerOpen) {
            Surface(
                tonalElevation = 4.dp,
                shadowElevation = 8.dp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
//                    .align(Alignment.TopStart)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Divider(modifier = Modifier.padding(bottom = 8.dp))
                    CategorySelector(viewModel = viewModel)
                }
            }
        }
}


@Composable
fun ProfileHeader(
    userName: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()
            .background(Color.White)) {
            // Top Row: "Profile" text and menu icon
            Row(
                modifier = Modifier

                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Color.Gray
                )

            }

            // Center: User icon and name
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Image",
                    modifier = Modifier.size(175.dp),
                    tint = Color.Gray
                )
                Text(
                    text = userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedArticles(articles: List<NewsArticle>, onArticleClick: () -> Unit) {
    Log.d("news-saved-articles","$articles")
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Saved Articles",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(articles) { article ->
                    ArticleCard(article,  onClick = onArticleClick)
                }
            }
        }
    }
}


@Composable
fun ArticleCard(article: NewsArticle, onClick: () -> Unit) {
    val context = LocalContext.current
    val intent = remember(article.articleLink) {
        Intent(Intent.ACTION_VIEW, Uri.parse(article.articleLink))
    }
    Box(
        modifier = Modifier
            .aspectRatio(0.75f)
            .clip(RoundedCornerShape(20.dp))
            .clickable {onClick()}
    ) {
        AsyncImage(
            model = article.imageUrl,
            contentDescription = article.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        startY = 300f
                    )
                )
        )

        // Title text at the bottom
        Text(
            text = article.title,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        )
    }
}




@Composable
fun CategorySelector(viewModel: CategoryViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    val categorySelection = viewModel.categorySelection
    val selectedCount = categorySelection.values.count { it }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Categories ($selectedCount/5)",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 14.sp
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // Collapsible content
        if (isExpanded) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(viewModel.getAllCategories()) { category ->
                    val isSelected = categorySelection[category.name] == true
                    val isTrending = category.name == "Trending"
                    val isEnabled = isTrending || isSelected || selectedCount < 5

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable(enabled = isEnabled && !isTrending) {
                                if (!isTrending) viewModel.toggleCategory(category.name)
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.name,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = category.name.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                )
                        )
                    }
                }
            }

            if (selectedCount == 5) {
                Text(
                    text = "Limit reached",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp, bottom = 4.dp)
                )
            }
        }
    }
}
