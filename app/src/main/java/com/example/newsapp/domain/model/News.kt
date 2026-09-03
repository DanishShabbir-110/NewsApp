package com.example.newsapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val title:String,
    val description:String?,
    val imageUrl:String?,
    val newsUrl: String,
    val sourceName: String,
    val publishedAt: String,
    val author: String?
)
