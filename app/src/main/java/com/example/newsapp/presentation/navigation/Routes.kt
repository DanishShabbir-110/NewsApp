package com.example.newsapp.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.example.newsapp.domain.model.News
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes : NavKey {
    @Serializable
    data object Main : Routes

    @Serializable
    data object Splash : Routes

    @Serializable
    data class Detail(val news: News) : Routes

}