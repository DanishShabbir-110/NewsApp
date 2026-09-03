package com.example.newsapp.presentation.newsdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.NewsTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewsDetailScreen(
    news: News,
    onBackClick: () -> Unit,
    viewModel: NewsDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(news) {
        viewModel.onIntent(NewsDetailIntent.SetNews(news))
    }
    NewsDetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun NewsDetailContent(
    uiState: NewsDetailUiState,
    onBackClick: () -> Unit,
    onIntent: (NewsDetailIntent) -> Unit
) {
    val news = uiState.news ?: return
    Scaffold(
        topBar = {
            NewsTopAppBar(
                title = "News Detail",
                showBackButton = true,
                onBackClick = onBackClick,
                showBookmarkButton = true,
                isBookmarked = uiState.isBookMarked,
                onBookmarkClick={
                    onIntent(NewsDetailIntent.BookmarkClick)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = news.imageUrl,
                contentDescription = news.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = news.sourceName,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (!news.author.isNullOrBlank()) {
                    Text(
                        text = "By ${news.author}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = news.publishedAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                if (!news.description.isNullOrBlank()) {
                    Text(
                        text = news.description,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}