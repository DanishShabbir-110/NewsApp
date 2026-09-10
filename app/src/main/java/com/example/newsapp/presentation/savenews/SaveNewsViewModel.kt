package com.example.newsapp.presentation.savenews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SaveNewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private var _uiState = MutableStateFlow(SaveNewsUiState())
    val uiState = _uiState.asStateFlow()
    private var saveNewsJob: Job? = null

    init {
        observeSaveNews()
    }

    fun onIntent(intent: SaveNewsIntent) {
        when (intent) {
            is SaveNewsIntent.RemoveNews -> {
                removeNews(intent.news)
            }

            SaveNewsIntent.Retry -> {
                observeSaveNews()
            }
        }
    }

    private fun removeNews(news: News) {
        viewModelScope.launch {
            try {
                repository.removeSavedNews(news.newsUrl)
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = ex.message ?: "Unable to remove saved news."
                    )
                }
            }
        }
    }

    private fun observeSaveNews() {
        saveNewsJob?.cancel()
        saveNewsJob = viewModelScope.launch {
            repository.getSavedNews().catch { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Something went wrong"
                    )
                }
            }.collectLatest { savedNews ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        news = savedNews,
                        error = null
                    )
                }
            }
        }
    }
}

data class SaveNewsUiState(
    val isLoading: Boolean = true,
    val news: List<News> = emptyList(),
    val error: String? = null
)

sealed class SaveNewsIntent {
    data class RemoveNews(val news: News) : SaveNewsIntent()

    data object Retry : SaveNewsIntent()
}