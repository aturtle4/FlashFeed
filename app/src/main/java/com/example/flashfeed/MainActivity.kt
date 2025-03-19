package com.example.flashfeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flashfeed.ui.theme.FlashFeedTheme
import com.example.flashfeed.reel_mechanism.NewsArticle
import com.example.flashfeed.reel_mechanism.NewsReelScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashFeedTheme {
                NewsApp()
            }
        }
    }
}


@Composable
fun NewsApp() {
    val sampleNews = listOf(
        NewsArticle(
            title = "Breaking News: AI Revolution",
            shortDescription = "AI is transforming industries worldwide...",
            mediumDescription = "AI is now being used in medical research, finance, and even journalism...",
            imageUrl = "https://img.freepik.com/premium-photo/3d-rendering-robot-artificial-intelligence-black-background-futuristic-technology-robot_844516-420.jpg",
            source = "Tech Times",
            timestamp = "2h ago",
            articleLink = "https://www.techtimes.com"
        ),
        NewsArticle(
            title = "Stock Market Crash Warning",
            shortDescription = "Experts predict a downturn in global markets...",
            mediumDescription = "With rising inflation and economic uncertainty, investors are worried about...",
            imageUrl = "https://source.unsplash.com/random/800x601",
            source = "Financial Daily",
            timestamp = "4h ago",
            articleLink = "https://www.financialdaily.com"
        )
    )

    NewsReelScreen(newsList = sampleNews)
}
