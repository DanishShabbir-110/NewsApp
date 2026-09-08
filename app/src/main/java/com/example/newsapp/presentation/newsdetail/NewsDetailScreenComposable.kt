package com.example.newsapp.presentation.newsdetail

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.newsapp.presentation.navigation.Routes
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.newsDetailScreenComposable(backStack: NavBackStack<NavKey>) {

    entry<Routes.Detail> { route ->
        val viewModel: NewsDetailViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        NewsDetailScreen(
            news = route.news,
            uiState = uiState,
            onBackClick = { backStack.removeLastOrNull() },
            onIntent = viewModel::onIntent
        )
    }
}