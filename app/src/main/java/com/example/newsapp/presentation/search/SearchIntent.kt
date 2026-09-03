package com.example.newsapp.presentation.search

sealed interface SearchIntent {
    data class QueryChanged(
        val query:String
    ): SearchIntent
    data object Search: SearchIntent
    data class RecentSearchClick(
        val query: String
    ): SearchIntent

    data object ClearRecentSearches: SearchIntent
    data object Retry: SearchIntent
}