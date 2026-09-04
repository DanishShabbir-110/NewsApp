package com.example.newsapp.presentation.savenews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.ErrorView
import com.example.newsapp.presentation.components.LoadingIndicator
import com.example.newsapp.presentation.components.NewsCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SaveNewsScreen(
    onNewsClick: (News) -> Unit,
    viewModel: SaveNewsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SaveNewsContent(
        uiState = uiState,
        onNewsClick = onNewsClick,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun SaveNewsContent(
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

@Composable
private fun SavedNewsList(news: List<News>, onNewsClick: (News) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = news,
            key = { it.newsUrl }
        ) { newsItem ->
            NewsCard(
                news = newsItem,
                onClick = { onNewsClick(newsItem) }
            )
        }
    }
}

@Composable
private fun EmptySavedNews() {
    Text(
        text = "No saved news yet.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(16.dp)
    )
}