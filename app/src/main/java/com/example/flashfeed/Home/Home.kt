package com.example.flashfeed.Home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashfeed.Profile.CategoryViewModel
import com.example.flashfeed.Profile.CategoryItem
import com.example.flashfeed.Profile.NewsReelViewModel
import com.example.flashfeed.reel_mechanism.NewsArticle
import com.example.flashfeed.reel_mechanism.NewsReelScreen

@Composable
fun Home(categoryViewModel: CategoryViewModel, newsReelViewModel: NewsReelViewModel) {
    val selectedCategories = categoryViewModel.getSelectedCategories()
    var selectedTab by remember { mutableStateOf(selectedCategories.firstOrNull()?.name ?: "Trending") }

    Box(modifier = Modifier.fillMaxSize()) {
        GetRespNews(category = selectedTab, newsReelViewModel)
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
    val inverseOnSurface = MaterialTheme.colorScheme.onSurface.run {
        Color(1f - red, 1f - green, 1f - blue, alpha)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        categories.forEach { category ->
            val isSelected = category.name == selectedTab

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
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else inverseOnSurface,
                )
                Text(
                    text = category.name.replaceFirstChar { it.uppercase() },
                    fontSize = 12.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else inverseOnSurface,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun GetRespNews(category: String, viewModel: NewsReelViewModel) {
    val dummyNewsArticles = listOf(
        NewsArticle(
            id = 1,
            title = "Trending Now: Solar Eclipse Captivates Millions",
            shortDescription = "A rare total solar eclipse stunned viewers across continents as skies turned dark and stars became visible in daylight.",
            mediumDescription = "The celestial event drew crowds to observatories and open fields, with many capturing the moment on social media. Astronomers explained the phenomena and safety tips for viewing, while educators used it as a live science demonstration for students.",
            imageUrl = "https://images.unsplash.com/photo-1504384764586-bb4cdc1707b0",
            source = "SkyWatch",
            timestamp = "1h ago",
            articleLink = "https://www.skywatch.com/eclipse",
            category = "Trending"
        ),
        NewsArticle(
            id = 2,
            title = "Government Unveils New Digital India Initiative",
            shortDescription = "Aiming for widespread connectivity, the government announced initiatives focused on digital literacy and infrastructure in rural areas.",
            mediumDescription = "The initiative includes high-speed internet for all villages, digital education centers, and mobile-first services. Officials say this move will improve access to government services and reduce the urban-rural digital divide.",
            imageUrl = "https://images.unsplash.com/photo-1581091870622-1b6ec1d7bb3b",
            source = "India Today",
            timestamp = "3h ago",
            articleLink = "https://www.indiatoday.in/digital-india",
            category = "National"
        ),
        NewsArticle(
            id = 3,
            title = "Stock Market Soars on Positive Earnings Reports",
            shortDescription = "Markets rallied today as major companies reported better-than-expected Q1 earnings, boosting investor confidence.",
            mediumDescription = "Tech and auto sectors led the surge, with analysts predicting continued growth. Experts warn of potential volatility due to global economic conditions, but remain optimistic about long-term trends.",
            imageUrl = "https://images.unsplash.com/photo-1565372910380-b22d90b4bd6c",
            source = "Economic Times",
            timestamp = "4h ago",
            articleLink = "https://www.economictimes.com/market-surge",
            category = "Business"
        ),
        NewsArticle(
            id = 4,
            title = "Cricket World Cup: India Secures Spot in Final",
            shortDescription = "With a thrilling win over Australia, India has booked its place in the World Cup finals, electrifying fans worldwide.",
            mediumDescription = "Virat Kohli led the chase with a spectacular century while bowlers delivered a disciplined performance. Social media erupted in celebration, and stadiums across India echoed with chants of victory.",
            imageUrl = "https://images.unsplash.com/photo-1579943026334-5b0eca5c1a3f",
            source = "Sports Central",
            timestamp = "6h ago",
            articleLink = "https://www.sportscentral.com/cricket-final",
            category = "Sports"
        ),
        NewsArticle(
            id = 5,
            title = "UN Urges Climate Action After Antarctic Melting Alarm",
            shortDescription = "New satellite data reveals accelerated ice loss in Antarctica, prompting global calls for urgent climate interventions.",
            mediumDescription = "The findings suggest sea levels could rise faster than previously predicted. Scientists urge nations to fulfill climate pledges and invest in sustainable solutions to prevent further ecological damage.",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
            source = "World News Daily",
            timestamp = "5h ago",
            articleLink = "https://www.worldnews.com/climate-warning",
            category = "World"
        ),
        NewsArticle(
            id = 6,
            title = "Parliament Passes Education Reform Bill",
            shortDescription = "The new bill focuses on reducing rote learning and encouraging creative thinking in schools across the country.",
            mediumDescription = "Highlights include the introduction of coding at an early stage, revamped assessment systems, and inclusive learning policies. Lawmakers debated extensively before reaching consensus.",
            imageUrl = "https://images.unsplash.com/photo-1596495577886-d920f1fb7238",
            source = "Parliament Press",
            timestamp = "7h ago",
            articleLink = "https://www.parliamentpress.in/education-bill",
            category = "Politics"
        ),
        NewsArticle(
            id = 7,
            title = "Breaking News: AI Revolution",
            shortDescription = "AI is reshaping industries at an unprecedented pace, from healthcare to finance, bringing automation and intelligence.",
            mediumDescription = "Artificial Intelligence is the driving force behind transformative changes. In healthcare, AI enables early diagnosis and robotic surgeries. In finance, it's revolutionizing customer service, fraud detection, and trading strategies. The tech community debates ethical concerns and privacy while pushing innovation forward.",
            imageUrl = "https://img.freepik.com/premium-photo/3d-rendering-robot-artificial-intelligence-black-background-futuristic-technology-robot_844516-420.jpg",
            source = "Tech Times",
            timestamp = "2h ago",
            articleLink = "https://www.techtimes.com",
            category = "Tech"
        ),
        NewsArticle(
            id = 8,
            title = "Indian Startup Launches Eco-Friendly Packaging",
            shortDescription = "A Bangalore-based startup introduces biodegradable alternatives to plastic packaging, gaining global attention.",
            mediumDescription = "Founded by three college graduates, the startup focuses on waste-reduction and green innovation. Their solution has been adopted by major e-commerce platforms and is now being exported to Europe and Southeast Asia.",
            imageUrl = "https://images.unsplash.com/photo-1581091012184-5c4c5bb1c1a6",
            source = "Startup Scoop",
            timestamp = "8h ago",
            articleLink = "https://www.startupscoop.com/eco-packaging",
            category = "Startup"
        ),
        NewsArticle(
            id = 9,
            title = "Bollywood Biopic Breaks Streaming Records",
            shortDescription = "The latest biopic on India's legendary athlete tops global charts within 24 hours of release.",
            mediumDescription = "Critics have praised the performance, direction, and storytelling. Streaming platforms report unprecedented viewership, and fans are calling for a sequel to explore more aspects of the athlete's journey.",
            imageUrl = "https://images.unsplash.com/photo-1582281298054-03d4b83ff6d1",
            source = "Cinema Express",
            timestamp = "9h ago",
            articleLink = "https://www.cinemaexpress.com/biopic-hit",
            category = "Ent"
        ),
        NewsArticle(
            id = 10,
            title = "10 Weird Facts You Won’t Believe Are True",
            shortDescription = "Did you know octopuses have three hearts? Here are 10 random but fascinating facts you probably didn’t know.",
            mediumDescription = "From upside-down waterfalls in Iceland to bananas being berries and strawberries not, the world is full of surprises. Learn and share these hatke facts at your next dinner table conversation.",
            imageUrl = "https://images.unsplash.com/photo-1608222345604-4d6a8c8e2f4b",
            source = "Buzzline",
            timestamp = "11h ago",
            articleLink = "https://www.buzzline.com/weird-facts",
            category = "Hatke"
        ),
        NewsArticle(
            id = 11,
            title = "Mystery of the Missing Asteroid Solved",
            shortDescription = "Scientists tracked down a long-lost asteroid thanks to new AI-enhanced telescopic systems.",
            mediumDescription = "The asteroid, missing from records for over 30 years, was found orbiting in an unexpected trajectory. The discovery helps researchers better understand gravitational anomalies and improves predictions for near-Earth objects.",
            imageUrl = "https://images.unsplash.com/photo-1517976487492-5750f3195933",
            source = "Science Daily",
            timestamp = "10h ago",
            articleLink = "https://www.sciencedaily.com/missing-asteroid",
            category = "Science"
        ),
        NewsArticle(
            id = 12,
            title = "Tesla Unveils Self-Repairing EV Prototype",
            shortDescription = "In a futuristic leap, Tesla showcases an electric vehicle that can self-heal minor body damage using smart materials.",
            mediumDescription = "The concept car uses heat-sensitive polymers and nanotechnology to detect and repair surface scratches and dents. Industry experts call it a game-changer for vehicle longevity and insurance claims.",
            imageUrl = "https://images.unsplash.com/photo-1603389491543-9a89df5d3f94",
            source = "AutoX",
            timestamp = "12h ago",
            articleLink = "https://www.autox.com/self-repairing-ev",
            category = "Auto"
        )
    )
    val filteredNews = dummyNewsArticles.filter { it.category == category }
    NewsReelScreen(newsList = filteredNews, viewModel)
}
