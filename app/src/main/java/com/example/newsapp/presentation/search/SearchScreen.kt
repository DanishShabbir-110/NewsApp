package com.example.newsapp.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.common.BaseScreen
import com.example.newsapp.presentation.components.LoadingIndicator
import com.example.newsapp.presentation.search.components.SearchResultList
import com.example.newsapp.presentation.search.components.SearchSuggestionContent
import com.example.newsapp.presentation.search.components.SearchTextField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onIntent: (SearchIntent) -> Unit,
    onNewsClick: (News) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Search",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        SearchTextField(
            query = uiState.query,
            onQueryChange = {
                onIntent(SearchIntent.QueryChanged(it))
            },
            onSearch = {
                onIntent(SearchIntent.Search)
            }
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (uiState.query.isBlank()) {
            SearchSuggestionContent(uiState = uiState, onIntent = onIntent)
        } else {
            BaseScreen(
                uiState = uiState,
                isLoading = { it.isLoading },
                error = { it.error },
                onRetry = { onIntent(SearchIntent.Retry) },
                loadingContent = {
                    LoadingIndicator("Searching news....")
                }
            ) { state ->
                if (state.news.isNotEmpty()) {
                    SearchResultList(
                        news = state.news,
                        onNewsClick = onNewsClick
                    )
                }
            }
        }
    }
}