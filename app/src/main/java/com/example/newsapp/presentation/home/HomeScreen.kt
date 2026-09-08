package com.example.newsapp.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.ErrorView
import com.example.newsapp.presentation.components.LoadingIndicator
import com.example.newsapp.presentation.home.components.CategorySection
import com.example.newsapp.presentation.home.components.NewsContent

@Composable
fun HomeScreen(uiState: HomeUiState, onIntent: (HomeIntent) -> Unit, onNewsClick: (News) -> Unit) {
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
                if (category == "All") onIntent(HomeIntent.SelectAllCategory)
                else onIntent(HomeIntent.SelectCategory(category.lowercase()))
            }
        )

        when {
            uiState.isLoading -> LoadingIndicator()

            uiState.error != null -> {
                ErrorView(
                    message = uiState.error,
                    onRetry = { onIntent(HomeIntent.Retry) }
                )
            }

            else -> {
                NewsContent(
                    news = uiState.news,
                    onNewsClick = onNewsClick,
                    onLoadMore = { onIntent(HomeIntent.LoadMore) },
                    isLoadingMore = uiState.isLoadingMore
                )
            }
        }
    }
}