package com.example.newsapp.presentation.newsdetail

import com.example.newsapp.domain.model.News

sealed interface NewsDetailIntent {
    data class SetNews(
        val news: News
    ): NewsDetailIntent
}