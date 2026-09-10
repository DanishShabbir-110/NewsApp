package com.example.newsapp.data.remote.dto

import com.example.newsapp.domain.model.News

data class NewsDto(
    val source: SourceDto,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

fun NewsDto.toNews(): News {
    return News(
        title = title,
        description = description,
        content = content,
        imageUrl = urlToImage,
        newsUrl = url,
        sourceName = source.name,
        publishedAt = publishedAt,
        author = author
    )
}
