package com.example.newsapp.presentation.savenews

import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SaveNewsViewModel(
    private val repository: NewsRepository
) : BaseViewModel<SaveNewsIntent, SaveNewsUiState>(SaveNewsUiState()) {

    init {
        observeSaveNews()
    }

    override fun onIntent(intent: SaveNewsIntent) {
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
                updateState {
                    it.copy(
                        isLoading = false,
                        error = ex.message ?: "Unable to remove saved news."
                    )
                }
            }
        }
    }

    private fun observeSaveNews() {
        viewModelScope.launch {
            repository.getSavedNews().catch { exception ->
                updateState {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Something went wrong"
                    )
                }
            }.collect { savedNews ->
                updateState {
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