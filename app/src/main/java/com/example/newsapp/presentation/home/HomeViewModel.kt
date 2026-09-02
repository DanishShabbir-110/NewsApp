package com.example.newsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: NewsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadNews()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SelectCategory -> {
                selectCategory(intent.category)
            }

            HomeIntent.Retry -> {
                loadNews(_uiState.value.selectedCategory)
            }
        }
    }

    private fun selectCategory(category: String) {
        _uiState.update {
            it.copy(
                selectedCategory = category
            )
        }

        loadNews()
    }

    private fun loadNews(category: String? = null) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {
                val news = repository.getTopHeadlines(category = category)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        news = news,
                        error = null
                    )
                }
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = ex.message ?: "Something went wrong."
                    )
                }
            }
        }

    }


}


data class HomeUiState(
    val isLoading: Boolean = false,
    val news: List<News> = emptyList(),
    val selectedCategory: String? = null,
    val error: String? = null
)