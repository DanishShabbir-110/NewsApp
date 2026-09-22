package com.example.newsapp.presentation.home

import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.model.NewsPage
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.presentation.common.BaseViewModel
import com.example.newsapp.utils.Constants.DEVELOPER_RESULT_LIMIT
import com.example.newsapp.utils.Constants.PAGE_SIZE
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: NewsRepository) :
    BaseViewModel<HomeIntent, HomeUiState>(
        HomeUiState()
    ) {

    private var currentPage = 1

    private var totalAvailableResults = Int.MAX_VALUE

    init {
        loadNews()
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {

            is HomeIntent.SelectCategory -> {
                selectCategory(intent.category)
            }

            HomeIntent.Retry -> {
                retry()
            }

            HomeIntent.LoadMore -> {
                loadMore()
            }

            HomeIntent.SelectAllCategory -> {
                selectAllCategory()
            }
        }
    }

    private fun selectAllCategory() {
        resetPagination()

        updateState {
            it.copy(
                selectedCategory = null,
                news = emptyList(),
                hasMoreData = true,
                error = null
            )
        }
        loadNews()
    }

    private fun loadMore() {
        val state = uiState.value

        if (state.isLoading || state.isLoadingMore || !state.hasMoreData) {
            return
        }

        if (getPageEndIndex(currentPage) > DEVELOPER_RESULT_LIMIT) {
            updateState {
                it.copy(
                    hasMoreData = false
                )
            }
            return
        }

        loadNews(
            category = state.selectedCategory,
            isLoadingMore = true
        )
    }

    private fun selectCategory(category: String) {
        resetPagination()

        updateState {
            it.copy(
                selectedCategory = category,
                news = emptyList(),
                hasMoreData = true,
                error = null
            )
        }

        loadNews(category = category)
    }

    private fun resetPagination() {
        currentPage = 1
        totalAvailableResults = Int.MAX_VALUE
    }

    private fun retry() {
        val state = uiState.value
        if (state.news.isEmpty()) {
            resetPagination()
            loadNews(category = state.selectedCategory)
        } else {
            loadMore()
        }
    }

    private fun loadNews(category: String? = null, isLoadingMore: Boolean = false) {
        viewModelScope.launch {
            updateLoadingState(isLoadingMore)
            try {
                val result = repository.getTopHeadlines(
                    category = category,
                    page = currentPage,
                    pageSize = PAGE_SIZE
                )
                println("DEBUG_HOME: API response received")
                println("DEBUG_HOME: Received news -> ${result.news.size}")
                println("DEBUG_HOME: Total results -> ${result.totalResult}")


                totalAvailableResults = minOf(
                    result.totalResult,
                    DEVELOPER_RESULT_LIMIT
                )

                updateNewsState(
                    result = result,
                    isLoadingMore = isLoadingMore
                )

                currentPage++

            } catch (ex: Exception) {

                updateState {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = ex.message
                            ?: "Something went wrong."
                    )
                }
            }
        }
    }
    private fun updateLoadingState(isLoadingMore: Boolean) {
        if (isLoadingMore) {
            updateState {
                it.copy(
                    isLoadingMore = true,
                    error = null
                )
            }
        } else {
            updateState {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
        }
    }

    private fun updateNewsState(
        result: NewsPage,
        isLoadingMore: Boolean
    ) {
        updateState { state ->

            val updatedNews =
                if (isLoadingMore) {
                    (state.news + result.news)
                        .distinctBy { it.newsUrl }
                } else {
                    result.news
                }

            val nextPage = currentPage + 1

            val hasMoreData =
                updatedNews.size < totalAvailableResults &&
                        result.news.isNotEmpty() &&
                        getPageEndIndex(nextPage) <= DEVELOPER_RESULT_LIMIT

            state.copy(
                isLoading = false,
                isLoadingMore = false,
                news = updatedNews,
                hasMoreData = hasMoreData,
                error = null
            )
        }
    }

    private fun getPageEndIndex(page: Int): Int {
        return page * PAGE_SIZE
    }

}

data class HomeUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true,
    val news: List<News> = emptyList(),
    val selectedCategory: String? = null,
    val error: String? = null
)

sealed class HomeIntent {
    data object SelectAllCategory : HomeIntent()
    data class SelectCategory(val category: String) : HomeIntent()
    data object LoadMore : HomeIntent()
    data object Retry : HomeIntent()
}