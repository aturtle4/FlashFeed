package com.example.flashfeed.reel_mechanism

import java.sql.Timestamp

data class NewsArticle (
    val id: Int,
    val title: String,
    val shortDescription: String,
    val mediumDescription: String,
    val imageUrl: String,
    val source: String,
    val timestamp: String,
    val articleLink: String,
    val category: String
)