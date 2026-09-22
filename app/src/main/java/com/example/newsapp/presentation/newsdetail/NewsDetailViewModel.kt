package com.example.newsapp.presentation.newsdetail

import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.presentation.common.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsDetailViewModel(
    private val repository: NewsRepository
) : BaseViewModel<NewsDetailIntent, NewsDetailUiState>(NewsDetailUiState()) {

    private var bookmarkJob: Job? = null

    override fun onIntent(intent: NewsDetailIntent) {
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
        val news = uiState.value.news ?: return
        val isBookMarked = uiState.value.isBookMarked

        viewModelScope.launch {
            if (isBookMarked) repository.removeSavedNews(news.newsUrl)
            else repository.saveNews(news)

        }
    }

    private fun setNews(news: News) {
        updateState {
            it.copy(news = news)
        }
        observeBookmarkState(news.newsUrl)
    }

    private fun observeBookmarkState(newsUrl: String) {
        bookmarkJob?.cancel()
        bookmarkJob=viewModelScope.launch {
            repository.isNewsSaved(newsUrl).collectLatest { isSaved ->
                updateState {
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
    data class SetNews(val news: News) : NewsDetailIntent()

    data object BookmarkClick : NewsDetailIntent()
}