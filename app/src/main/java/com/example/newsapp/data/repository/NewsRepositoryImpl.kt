package com.example.newsapp.data.repository

import com.example.newsapp.data.local.database.dao.NewsDao
import com.example.newsapp.data.local.database.entity.toNews
import com.example.newsapp.data.local.database.entity.toSavedNewsEntity
import com.example.newsapp.data.remote.api.NewsApiService
import com.example.newsapp.data.remote.dto.toNews
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.model.NewsPage
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsRepositoryImpl(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao
) : NewsRepository {
    override suspend fun getTopHeadlines(category: String?, page: Int, pageSize: Int): NewsPage {
        val response = apiService.getTopHeadlines(category = category, page = page, pageSize = pageSize)
        return NewsPage(
            news = response.articles.map { it.toNews() },
            totalResult = response.totalResults
        )
    }

    override suspend fun searchNews(query: String): List<News> {
        val response = apiService.searchNews(query)
        return response.articles.map {
            it.toNews()
        }
    }

    override suspend fun saveNews(news: News) {
        newsDao.saveNews(news.toSavedNewsEntity())
    }

    override suspend fun removeSavedNews(newsUrl: String) {
        newsDao.deleteNews(newsUrl)
    }

    override fun getSavedNews(): Flow<List<News>> {
        return newsDao.getSavedNews().map { newsEntities ->
            newsEntities.map { entity ->
                entity.toNews()
            }
        }
    }

    override fun isNewsSaved(newsUrl: String): Flow<Boolean> {
        return newsDao.isNewsSaved(newsUrl)
    }


}