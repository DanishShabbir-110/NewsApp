package com.example.newsapp.presentation.home

sealed interface HomeIntent {
    data class SelectCategory(
        val category: String
    ) : HomeIntent

    data object LoadMore: HomeIntent

    data object Retry : HomeIntent
}