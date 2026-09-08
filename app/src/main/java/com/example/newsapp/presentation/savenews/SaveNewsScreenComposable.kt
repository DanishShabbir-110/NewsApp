package com.example.newsapp.presentation.savenews

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.newsapp.presentation.navigation.Routes
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.saveNewsScreenComposable(backStack: NavBackStack<NavKey>) {

    entry<Routes.SaveNews> {
        val viewModel: SaveNewsViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        SaveNewsScreen(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            onNewsClick = { news ->
                backStack.add(Routes.Detail(news = news))
            }
        )
    }
}