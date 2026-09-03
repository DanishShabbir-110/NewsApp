package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.model.NewsPage
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines(
        category: String? = null,
        page:Int,
        pageSize:Int
    ): NewsPage

    suspend fun searchNews(
        query: String
    ): List<News>

    suspend fun saveNews(
        news: News
    )

    suspend fun removeSavedNews(
        newsUrl: String
    )

    fun getSavedNews(): Flow<List<News>>

    fun isNewsSaved(
        newsUrl: String
    ): Flow<Boolean>
}