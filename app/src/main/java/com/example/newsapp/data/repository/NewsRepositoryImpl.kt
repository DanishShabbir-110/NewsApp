package com.example.newsapp.data.repository

import com.example.newsapp.data.remote.api.NewsApiService
import com.example.newsapp.data.remote.dto.toNews
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val apiService: NewsApiService
) : NewsRepository {
    override suspend fun getTopHeadlines(category: String?): List<News> {
        val response = apiService.getTopHeadlines(category = category)
        return response.articles.map {
            it.toNews()
        }
    }

    override suspend fun searchNews(query: String): List<News> {
        val response = apiService.searchNews(query)
        return response.articles.map {
            it.toNews()
        }
    }

}