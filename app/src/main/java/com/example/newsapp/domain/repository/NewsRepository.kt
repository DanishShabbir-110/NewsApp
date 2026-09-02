package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines(
        category: String? = null
    ): List<News>

    suspend fun searchNews(
        query: String
    ): List<News>

//    suspend fun savedNews(
//        news: News
//    )
//
//    suspend fun removedSavedNews(
//        newsUrl: String
//    )
//
//    fun getSavedNews(): Flow<List<News>>
//
//    fun isNewsSaved(
//        newsUrl: String
//    ): Flow<Boolean>
}