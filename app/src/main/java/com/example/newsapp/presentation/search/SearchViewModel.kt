package com.example.newsapp.presentation.search

import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.sharedpreferences.SearchPreferences
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.presentation.common.BaseViewModel
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: NewsRepository,
    private val searchPreferences: SearchPreferences
) : BaseViewModel<SearchIntent, SearchUiState>(SearchUiState(recentSearches = searchPreferences.getRecentSearches())) {
    override fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> {
                updateQuery(intent.query)
            }

            SearchIntent.Search -> {
                searchNews()
            }

            is SearchIntent.RecentSearchClick -> {
                recentSearchClick(intent.query)
            }

            SearchIntent.ClearRecentSearches -> {
                clearRecentSearches()
            }

            SearchIntent.Retry -> {
                searchNews()
            }
        }
    }

    private fun addRecentSearch(query: String) {
        searchPreferences.saveRecentSearcher(query)
        updateState { state ->
            state.copy(
                recentSearches = searchPreferences.getRecentSearches()
            )
        }
    }

    private fun recentSearchClick(query: String) {

        updateState {
            it.copy(query = query)
        }

        searchNews()
    }

    private fun clearRecentSearches() {
        searchPreferences.clearRecentSearches()
        updateState {
            it.copy(
                recentSearches = emptyList()
            )
        }
    }

    private fun searchNews() {
        val query = uiState.value.query.trim()
        if (query.isEmpty()) return
        addRecentSearch(query)
        viewModelScope.launch {
            updateState {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {
                val result = repository.searchNews(query)
                updateState {
                    it.copy(
                        isLoading = false,
                        news = result,
                        error = null
                    )
                }
            } catch (ex: Exception) {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = ex.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    private fun updateQuery(query: String) {
        updateState {
            it.copy(
                query = query,
                news = if (query.isBlank()) emptyList() else it.news,
                error = null
            )
        }
    }

}

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val news: List<News> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val error: String? = null
)

sealed class SearchIntent {
    data class QueryChanged(val query: String) : SearchIntent()
    data object Search : SearchIntent()
    data class RecentSearchClick(val query: String) : SearchIntent()
    data object ClearRecentSearches : SearchIntent()
    data object Retry : SearchIntent()
}