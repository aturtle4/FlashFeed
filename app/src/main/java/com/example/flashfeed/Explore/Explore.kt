package com.example.flashfeed.Explore

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val newsList = listOf(
        NewsArticle(
            id = 1,
            title = "Trending Now: Solar Eclipse Captivates Millions",
            content = "A rare total solar eclipse stunned viewers across continents as skies turned dark and stars became visible in daylight.",
            imageUrl = "https://images.unsplash.com/photo-1504384764586-bb4cdc1707b0",
            source = "SkyWatch",
            timestamp = "1h ago",
            articleLink = "https://www.skywatch.com/eclipse",
            category = "Trending"
        ),
        NewsArticle(
            id = 2,
            title = "Government Unveils New Digital India Initiative",
            content = "Aiming for widespread connectivity, the government announced initiatives focused on digital literacy and infrastructure in rural areas.",
            imageUrl = "https://images.unsplash.com/photo-1581091870622-1b6ec1d7bb3b",
            source = "India Today",
            timestamp = "3h ago",
            articleLink = "https://www.indiatoday.in/digital-india",
            category = "National"
        ),
        NewsArticle(
            id = 3,
            title = "Stock Market Soars on Positive Earnings Reports",
            content = "Markets rallied today as major companies reported better-than-expected Q1 earnings, boosting investor confidence.",
            imageUrl = "https://images.unsplash.com/photo-1565372910380-b22d90b4bd6c",
            source = "Economic Times",
            timestamp = "4h ago",
            articleLink = "https://www.economictimes.com/market-surge",
            category = "Business"
        ),
        NewsArticle(
            id = 4,
            title = "Cricket World Cup: India Secures Spot in Final",
            content = "With a thrilling win over Australia, India has booked its place in the World Cup finals, electrifying fans worldwide.",
            imageUrl = "https://images.unsplash.com/photo-1579943026334-5b0eca5c1a3f",
            source = "Sports Central",
            timestamp = "6h ago",
            articleLink = "https://www.sportscentral.com/cricket-final",
            category = "Sports"
        ),
        NewsArticle(
            id = 5,
            title = "UN Urges Climate Action After Antarctic Melting Alarm",
            content = "New satellite data reveals accelerated ice loss in Antarctica, prompting global calls for urgent climate interventions.",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
            source = "World News Daily",
            timestamp = "5h ago",
            articleLink = "https://www.worldnews.com/climate-warning",
            category = "World"
        ),
        NewsArticle(
            id = 6,
            title = "Parliament Passes Education Reform Bill",
            content = "The new bill focuses on reducing rote learning and encouraging creative thinking in schools across the country.",
            imageUrl = "https://images.unsplash.com/photo-1596495577886-d920f1fb7238",
            source = "Parliament Press",
            timestamp = "7h ago",
            articleLink = "https://www.parliamentpress.in/education-bill",
            category = "Politics"
        ),
        NewsArticle(
            id = 7,
            title = "Breaking News: AI Revolution",
            content = "AI is reshaping industries at an unprecedented pace, from healthcare to finance, bringing automation and intelligence.",
            imageUrl = "https://img.freepik.com/premium-photo/3d-rendering-robot-artificial-intelligence-black-background-futuristic-technology-robot_844516-420.jpg",
            source = "Tech Times",
            timestamp = "2h ago",
            articleLink = "https://www.techtimes.com",
            category = "Tech"
        ),
        NewsArticle(
            id = 8,
            title = "Indian Startup Launches Eco-Friendly Packaging",
            content = "A Bangalore-based startup introduces biodegradable alternatives to plastic packaging, gaining global attention.",
            imageUrl = "https://images.unsplash.com/photo-1581091012184-5c4c5bb1c1a6",
            source = "Startup Scoop",
            timestamp = "8h ago",
            articleLink = "https://www.startupscoop.com/eco-packaging",
            category = "Startup"
        ),
        NewsArticle(
            id = 9,
            title = "Bollywood Biopic Breaks Streaming Records",
            content = "The latest biopic on India's legendary athlete tops global charts within 24 hours of release.",
            imageUrl = "https://images.unsplash.com/photo-1582281298054-03d4b83ff6d1",
            source = "Cinema Express",
            timestamp = "9h ago",
            articleLink = "https://www.cinemaexpress.com/biopic-hit",
            category = "Ent"
        ),
    )
    val categories = categoryViewModel.getAllCategories()
    when {
        launchNews -> {
            NewsReelContainer(newsList = newsList, newsReelViewModel = newsReelViewModel, onBack = { launchNews = false })
        }
        catLaunchNews.first -> {
            val filteredNews = newsList.filter { it.category == catLaunchNews.second }
            NewsReelContainer(newsList = filteredNews, newsReelViewModel = newsReelViewModel, currentCategory = catLaunchNews.second, onBack = {
                catLaunchNews = Pair(false, catLaunchNews.second)
            })
        }
        else -> {
            ExploreContent(
                categories = categories,
                newsList = newsList,
                onCategoryClick = { selectedCategory ->
                    catLaunchNews = Pair(true, selectedCategory)
                },
                onNewsClick = {
                    launchNews = true
                }
            )
        }
    }
}

@Composable
fun NewsReelContainer(
    newsList: List<NewsArticle>,
    newsReelViewModel: NewsReelViewModel,
    currentCategory: String? = null,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NewsReelScreen(newsList, newsReelViewModel, currentCategory.toString())
        FloatingActionButton(
            onClick = onBack,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun ExploreContent(
    categories: List<CategoryItem>,
    newsList: List<NewsArticle>,
    onCategoryClick: (String) -> Unit,
    onNewsClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Explore",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    CategoryItemCard(category = category, onClick = { onCategoryClick(category.name) })
                }
            }
        }
        item {
            Text(
                text = "Popular This Week",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(newsList) { news ->
                    NewsTile(news = news, onClick = onNewsClick)
                }
            }
        }
    }
}

@Composable
fun NewsTile(news: NewsArticle, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 8.dp,
        modifier = Modifier
            .aspectRatio(0.75f)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = news.imageUrl,
                contentDescription = news.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                            startY = 200f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = news.title,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = news.timestamp,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryItemCard(category: CategoryItem, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


