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
        @Query("initial") initial: String
    ): List<NewsArticle>
}

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.46.92:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: NewsAPI = retrofit.create(NewsAPI::class.java)
}
