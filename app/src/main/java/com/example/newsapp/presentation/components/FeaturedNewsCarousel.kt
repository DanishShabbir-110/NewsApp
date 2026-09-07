package com.example.newsapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsapp.domain.model.News
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FeaturedNewsCarousel(
    news: List<News>,
    onNewsClick: (News) -> Unit
) {
    val featuredNews = news.take(3)
    val pageState = rememberPagerState(pageCount = { featuredNews.size })
    LaunchedEffect(featuredNews.size) {
        if (featuredNews.size <= 1) {
            return@LaunchedEffect
        }
        while (true) {
            delay(2000.milliseconds)
            val nextPage = (pageState.currentPage + 1) % featuredNews.size
            pageState.animateScrollToPage(page = nextPage)
        }
    }
    Column {
        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val newsItem = featuredNews[page]
            FeaturedNewsCard(
                news = newsItem,
                onClick = { onNewsClick(newsItem) }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            featuredNews.forEachIndexed { index, _ ->
                val isSelected = index == pageState.currentPage

                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .width(if (isSelected) 28.dp else 18.dp)
                        .height(3.dp)
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = MaterialTheme.shapes.small
                        )
                ) {

                }
            }
        }
    }
}