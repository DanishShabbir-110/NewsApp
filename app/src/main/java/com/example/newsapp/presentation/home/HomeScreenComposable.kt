package com.example.newsapp.presentation.home

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.newsapp.presentation.navigation.Routes
import org.koin.androidx.compose.koinViewModel


fun EntryProviderScope<NavKey>.homeScreenComposable(backStack: NavBackStack<NavKey>) {
    entry<Routes.Home> {
        val viewModel: HomeViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        HomeScreen(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            onNewsClick = {
                backStack.add(Routes.Detail(it))
            }
        )
    }
}