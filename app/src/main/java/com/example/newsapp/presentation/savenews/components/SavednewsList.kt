package com.example.newsapp.presentation.savenews.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.NewsCard

@Composable
fun SavedNewsList(news: List<News>, onNewsClick: (News) -> Unit) {
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