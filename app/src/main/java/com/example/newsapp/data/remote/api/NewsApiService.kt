package com.example.newsapp.data.remote.api

import com.example.newsapp.data.remote.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("category") category: String? = null
    ): NewsResponseDto

    @GET("/everything")
    suspend fun searchNews(@Query("q") query: String): NewsResponseDto

}