package com.example.newsapp.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.FeaturedNewsCarousel
import com.example.newsapp.presentation.components.LoadingIndicator
import com.example.newsapp.presentation.components.NewsCard
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun NewsContent(
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

            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            val totalItems = layoutInfo.totalItemsCount

            lastVisibleItemIndex >= totalItems - 2
        }
            .distinctUntilChanged()
            .collect { loadMore ->

                if (loadMore && !isLoadingMore) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            FeaturedNewsCarousel(news = featuredNews, onNewsClick = onNewsClick)
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