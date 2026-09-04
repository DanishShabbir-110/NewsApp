package com.example.newsapp.presentation.savenews

import com.example.newsapp.domain.model.News

sealed interface SaveNewsIntent {
    data class RemoveNews(
        val news: News
    ): SaveNewsIntent

    data object Retry: SaveNewsIntent
}