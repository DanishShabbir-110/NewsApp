package com.example.newsapp.domain.model

data class News(
    val title:String,
    val description:String?,
    val content:String?,
    val imageUrl:String?,
    val newsUrl: String,
    val sourceName: String,
    val publishedAt: String,
    val author: String?
)
