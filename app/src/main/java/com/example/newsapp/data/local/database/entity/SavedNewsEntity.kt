package com.example.newsapp.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.domain.model.News

@Entity("saved_news")
data class SavedNewsEntity(
    @PrimaryKey
    @ColumnInfo("news_url")
    val newsUrl: String,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("description")
    val description: String?,
    @ColumnInfo("content")
    val content: String?,
    @ColumnInfo("image_url")
    val imageUrl: String?,
    @ColumnInfo("source_name")
    val sourceName: String?,
    @ColumnInfo("published_at")
    val publishedAt: String,
    @ColumnInfo("author_name")
    val author: String?
)

fun News.toSavedNewsEntity(): SavedNewsEntity {
    return SavedNewsEntity(
        newsUrl = newsUrl,
        title = title,
        description = description,
        content = content,
        imageUrl = imageUrl,
        sourceName = sourceName,
        publishedAt = publishedAt,
        author = author
    )
}

fun SavedNewsEntity.toNews(): News {
    return News(
        newsUrl = newsUrl,
        title = title,
        description = description,
        content = content,
        imageUrl = imageUrl,
        sourceName = sourceName,
        publishedAt = publishedAt,
        author = author
    )
}
