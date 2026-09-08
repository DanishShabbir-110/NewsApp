package com.example.newsapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.sharedpreferences.SearchPreferences
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: NewsRepository,
    private val searchPreferences: SearchPreferences
) : ViewModel() {
    private var _uiState =
        MutableStateFlow(SearchUiState(recentSearches = searchPreferences.getRecentSearches()))
    val state = _uiState.asStateFlow()

    fun onIntent(intent: SearchIntent) {
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
        _uiState.update { state ->
            state.copy(
                recentSearches = searchPreferences.getRecentSearches()
            )
        }
    }

    private fun recentSearchClick(query: String) {

        _uiState.update {
            it.copy(query = query)
        }

        searchNews()
    }

    private fun clearRecentSearches() {
        searchPreferences.clearRecentSearches()
        _uiState.update {
            it.copy(
                recentSearches = emptyList()
            )
        }
    }

    private fun searchNews() {
        val query = _uiState.value.query.trim()
        if (query.isEmpty()) return
        addRecentSearch(query)
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {
                val result = repository.searchNews(query)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        news = result,
                        error = null
                    )
                }
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = ex.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    private fun updateQuery(query: String) {
        _uiState.update {
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