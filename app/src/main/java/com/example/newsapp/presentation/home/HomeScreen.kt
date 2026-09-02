package com.example.newsapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.CategoryChip
import com.example.newsapp.presentation.components.ErrorView
import com.example.newsapp.presentation.components.FeaturedNewsCard
import com.example.newsapp.presentation.components.LoadingIndicator
import com.example.newsapp.presentation.components.NewsCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNewsClick: (News) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onNewsClick = onNewsClick
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    onNewsClick: (News) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "News",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 16.dp)
        )

        CategorySection(
            selectedCategory = uiState.selectedCategory,
            onCategoryClick = {
                onIntent(
                    HomeIntent.SelectCategory(it)
                )
            }
        )

        when {
            uiState.isLoading -> {
                LoadingIndicator()
            }

            uiState.error != null -> {
                ErrorView(
                    message = uiState.error,
                    onRetry = {
                        onIntent(HomeIntent.Retry)
                    }
                )
            }

            else -> {
                NewsContent(
                    news = uiState.news,
                    onNewsClick = onNewsClick
                )
            }
        }
    }
}

@Composable
private fun NewsContent(
    news: List<News>,
    onNewsClick: (News) -> Unit
) {
    if (news.isEmpty()) {
        Text(
            text = "No News Available.",
            modifier = Modifier.padding(vertical = 16.dp)
        )
        return
    }

    val featuredNews = news.first()
    val remainingNews = news.drop(1)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            FeaturedNewsCard(
                news = featuredNews,
                onClick = {
                    onNewsClick(featuredNews)
                }
            )
        }

        if (remainingNews.isNotEmpty()) {
            item {
                Text(
                    text = "Top Headlines",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        items(
            items = remainingNews,
            key = {
                it.newsUrl
            }
        ) { newsItem ->

            NewsCard(
                news = newsItem,
                onClick = {
                    onNewsClick(newsItem)
                }
            )
        }
    }
}

@Composable
private fun CategorySection(
    selectedCategory: String?,
    onCategoryClick: (String) -> Unit
) {
    val categories = listOf(
        "general",
        "business",
        "technology",
        "sports",
        "health",
        "science",
        "entertainment"
    )

    LazyRow(
        contentPadding = PaddingValues(
            vertical = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(categories) { category ->

            CategoryChip(
                title = category.replaceFirstChar {
                    it.uppercase()
                },
                isSelected = selectedCategory == category,
                onClick = {
                    onCategoryClick(category)
                }
            )
        }
    }
}