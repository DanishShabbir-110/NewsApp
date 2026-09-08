package com.example.newsapp.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.NewsCard

@Composable
fun SearchResultList(news: List<News>, onNewsClick: (News) -> Unit) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            bottom = 24.dp
        )
    ) {

        item {

            Text(
                text = "Search Results",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "${news.size} results found",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        items(
            items = news,
            key = { it.newsUrl }
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