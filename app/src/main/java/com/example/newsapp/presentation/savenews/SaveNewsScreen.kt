package com.example.newsapp.presentation.savenews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.ErrorView
import com.example.newsapp.presentation.components.LoadingIndicator
import com.example.newsapp.presentation.savenews.components.EmptySavedNews
import com.example.newsapp.presentation.savenews.components.SavedNewsList

@Composable
fun SaveNewsScreen(
    uiState: SaveNewsUiState,
    onNewsClick: (News) -> Unit,
    onIntent: (SaveNewsIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Text(
            text = "Saved News",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        when {
            uiState.isLoading -> {
                LoadingIndicator(text = "Loading saved news...")
            }

            uiState.error != null -> {
                ErrorView(
                    message = uiState.error,
                    onRetry = {
                        onIntent(SaveNewsIntent.Retry)
                    }
                )
            }

            uiState.news.isEmpty() -> {
                EmptySavedNews()
            }

            else -> {
                SavedNewsList(
                    news = uiState.news,
                    onNewsClick = onNewsClick
                )
            }
        }
    }
}
