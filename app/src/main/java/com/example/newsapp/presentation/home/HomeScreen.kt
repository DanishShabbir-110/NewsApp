package com.example.newsapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.CategoryChip
import com.example.newsapp.presentation.components.ErrorView
import com.example.newsapp.presentation.components.FeaturedNewsCard
import com.example.newsapp.presentation.components.FeaturedNewsCarousel
import com.example.newsapp.presentation.components.LoadingIndicator
import com.example.newsapp.presentation.components.NewsCard
import kotlinx.coroutines.flow.distinctUntilChanged
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
            onCategoryClick = { category ->

                if (category == "All") {

                    onIntent(
                        HomeIntent.SelectAllCategory
                    )

                } else {

                    onIntent(
                        HomeIntent.SelectCategory(
                            category.lowercase()
                        )
                    )
                }
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
                    onNewsClick = onNewsClick,
                    onLoadMore = {
                        onIntent(HomeIntent.LoadMore)
                    },
                    isLoadingMore = uiState.isLoadingMore
                )
            }
        }
    }
}

@Composable
private fun NewsContent(
    news: List<News>,
    isLoadingMore: Boolean,
    onNewsClick: (News) -> Unit,
    onLoadMore: () -> Unit
) {

    if (news.isEmpty()) {
        Text(
            text = "No News Available.",
            modifier = Modifier.padding(vertical = 16.dp)
        )
        return
    }

    val listState = rememberLazyListState()

    val featuredNews = news.take(3)
    val remainingNews = news.drop(3)

    LaunchedEffect(listState, news.size) {

        snapshotFlow {
            val layoutInfo = listState.layoutInfo

            val lastVisibleItemIndex =
                layoutInfo.visibleItemsInfo
                    .lastOrNull()
                    ?.index ?: 0

            val totalItems =
                layoutInfo.totalItemsCount

            lastVisibleItemIndex >= totalItems - 2
        }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->

                if (shouldLoadMore && !isLoadingMore) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            FeaturedNewsCarousel(news = featuredNews, onNewsClick=onNewsClick)
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
            key = { it.newsUrl }
        ) { newsItem ->

            NewsCard(
                news = newsItem,
                onClick = {
                    onNewsClick(newsItem)
                }
            )
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }
        }
    }
}

@Composable
private fun CategorySection(
    selectedCategory: String?,
    onCategoryClick: (String) -> Unit
) {

    val categories = listOf(
        "All",
        "Business",
        "Technology",
        "Sports",
        "Health",
        "Science",
        "Entertainment"
    )

    LazyRow(
        contentPadding = PaddingValues(
            vertical = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(
            items = categories,
            key = { it }
        ) { category ->

            CategoryChip(
                title = category,

                isSelected =
                    if (category == "All") {
                        selectedCategory == null
                    } else {
                        selectedCategory == category.lowercase()
                    },

                onClick = {
                    onCategoryClick(category)
                }
            )
        }
    }
}