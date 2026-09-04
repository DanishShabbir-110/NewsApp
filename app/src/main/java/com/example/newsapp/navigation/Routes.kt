package com.example.newsapp.navigation

import androidx.navigation3.runtime.NavKey
import com.example.newsapp.domain.model.News
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes: NavKey {
    @Serializable
    data object Home: Routes
    @Serializable
    data class Detail(
        val news: News
    ) : Routes
    @Serializable
    data object Search:Routes
    @Serializable
    data object SaveNews: Routes
}