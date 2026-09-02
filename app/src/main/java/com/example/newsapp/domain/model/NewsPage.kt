package com.example.newsapp.domain.model

data class NewsPage(
    val news: List<News>,
    val totalResult: Int
)
