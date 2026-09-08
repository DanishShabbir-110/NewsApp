package com.example.newsapp.presentation.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.presentation.search.SearchIntent
import com.example.newsapp.presentation.search.SearchUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchSuggestionContent(uiState: SearchUiState, onIntent: (SearchIntent) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                            onIntent(SearchIntent.RecentSearchClick(search))
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
                    onIntent(SearchIntent.RecentSearchClick(trending))
                }
            )
        }
    }
}