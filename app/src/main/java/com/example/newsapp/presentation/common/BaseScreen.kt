package com.example.newsapp.presentation.common

import androidx.compose.runtime.Composable
import com.example.newsapp.presentation.components.ErrorView
import com.example.newsapp.presentation.components.LoadingIndicator

@Composable
fun <T> BaseScreen(
    uiState: T,
    isLoading: (T) -> Boolean,
    error: (T) -> String?,
    onRetry: () -> Unit,
    loadingContent: @Composable () -> Unit = {
        LoadingIndicator()
    },
    content: @Composable (T) -> Unit
) {
    when {
        isLoading(uiState) -> loadingContent()
        error(uiState) != null -> {
            ErrorView(
                message = error(uiState)!!,
                onRetry = onRetry
            )
        }

        else -> content(uiState)
    }
}