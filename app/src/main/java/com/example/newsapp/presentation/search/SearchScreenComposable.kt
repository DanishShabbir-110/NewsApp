package com.example.newsapp.presentation.search

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.newsapp.presentation.navigation.Routes
import org.koin.androidx.compose.koinViewModel

fun EntryProviderScope<NavKey>.searchScreenComposable(backStack: NavBackStack<NavKey>) {

    entry<Routes.Search> {
        val viewModel: SearchViewModel = koinViewModel()
        val uiState by viewModel.state.collectAsStateWithLifecycle()

        SearchScreen(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            onNewsClick = { news ->
                backStack.add(Routes.Detail(news = news))
            }
        )
    }

}