package com.example.flashfeed.API

import com.example.flashfeed.reel_mechanism.NewsArticle
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsAPI {
    @GET("news/fetch")
    suspend fun getNewsByCategory(
        @Query("category") category: String,
        @Query("count") count: Int = 10,
        @Query("initial") initial: String,
        @Query("language") language: String = "English"
    ): List<NewsArticle>
}

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://flashfeed-go5j.onrender.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: NewsAPI = retrofit.create(NewsAPI::class.java)
}
