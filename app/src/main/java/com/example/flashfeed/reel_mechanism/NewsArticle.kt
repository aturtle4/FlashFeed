package com.example.flashfeed.reel_mechanism

import java.sql.Timestamp

data class NewsArticle (
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String,
    val source: String,
    val timestamp: String,
    val articleLink: String,
    val category: String
)