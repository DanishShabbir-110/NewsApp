package com.example.newsapp.presentation.newsdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsDetailViewModel(private val repository: NewsRepository) : ViewModel() {
    private var _uiState = MutableStateFlow(NewsDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: NewsDetailIntent) {
        when (intent) {
            is NewsDetailIntent.SetNews -> {
                setNews(intent.news)
            }

            NewsDetailIntent.BookmarkClick -> {
                toggleBookmark()
            }
        }
    }

    private fun toggleBookmark() {
        val news = _uiState.value.news ?: return
        val isBookMarked = _uiState.value.isBookMarked

        viewModelScope.launch {
            if (isBookMarked) repository.removeSavedNews(news.newsUrl)
            else repository.saveNews(news)

        }
    }

    private fun setNews(news: News) {
        _uiState.update {
            it.copy(news = news)
        }
        observeBookmarkState(news.newsUrl)
    }

    private fun observeBookmarkState(newsUrl: String) {
        viewModelScope.launch {
            repository.isNewsSaved(newsUrl).collectLatest { isSaved ->
                _uiState.update {
                    it.copy(
                        isBookMarked = isSaved
                    )
                }
            }
        }
    }
}

data class NewsDetailUiState(
    val news: News? = null,
    val isBookMarked: Boolean = false
)

sealed class NewsDetailIntent {
    data class SetNews(val news: News): NewsDetailIntent()

    data object BookmarkClick: NewsDetailIntent()
}