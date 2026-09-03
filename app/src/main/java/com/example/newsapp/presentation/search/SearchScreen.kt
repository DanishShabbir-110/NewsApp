package com.example.newsapp.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.EmptyState
import com.example.newsapp.presentation.components.ErrorView
import com.example.newsapp.presentation.components.LoadingIndicator
import com.example.newsapp.presentation.components.NewsCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onNewsClick: (News) -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    SearchContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onNewsClick = onNewsClick
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchContent(
    uiState: SearchUiState,
    onIntent: (SearchIntent) -> Unit,
    onNewsClick: (News) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize().statusBarsPadding()
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

        when {
            uiState.query.isBlank()->{
                SearchSuggestionContent(
                    uiState = uiState,
                    onIntent=onIntent
                )
            }
            uiState.isLoading -> {

                LoadingIndicator(
                    text = "Searching news..."
                )
            }

            uiState.error != null -> {

                ErrorView(
                    message = uiState.error,
                    onRetry = {
                        onIntent(SearchIntent.Retry)
                    }
                )
            }

            uiState.news.isNotEmpty() -> {

                SearchResultList(
                    news = uiState.news,
                    onNewsClick = onNewsClick
                )
            }

            uiState.query.isNotBlank() -> {
                EmptyState(
                    message = "No news found"
                )
            }

            else -> {

                SearchSuggestionContent(
                    uiState = uiState,
                    onIntent = onIntent
                )
            }
        }
    }
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {

    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Search news..."
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search"
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),

        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),

        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        ),

        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    )
}

@Composable
private fun RecentSearchChip(
    text: String,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier.clickable {
            onClick()
        },
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = null,
                modifier = Modifier
                    .width(16.dp)
                    .height(16.dp)
            )

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Text(
                text = text,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun TrendingSearchItem(
    text: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = text,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun SearchResultList(
    news: List<News>,
    onNewsClick: (News) -> Unit
) {

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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchSuggestionContent(
    uiState: SearchUiState,
    onIntent: (SearchIntent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {

        if (uiState.recentSearches.isNotEmpty()) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Recent Searches",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Text(
                    text = "Clear all",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onIntent(
                            SearchIntent.ClearRecentSearches
                        )
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                uiState.recentSearches.forEach { search ->

                    RecentSearchChip(
                        text = search,
                        onClick = {
                            onIntent(
                                SearchIntent.RecentSearchClick(search)
                            )
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )
        }

        Text(
            text = "Trending Searches",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        val trendingSearches = listOf(
            "Artificial Intelligence",
            "Bitcoin",
            "Technology",
            "Climate Change",
            "OpenAI",
            "Stock Market"
        )

        trendingSearches.forEach { trending ->

            TrendingSearchItem(
                text = trending,
                onClick = {
                    onIntent(
                        SearchIntent.RecentSearchClick(trending)
                    )
                }
            )
        }
    }
}