package com.example.newsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.utils.Constants.DEVELOPER_RESULT_LIMIT
import com.example.newsapp.utils.Constants.PAGE_SIZE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private var currentPage = 1

    private var totalAvailableResults = Int.MAX_VALUE

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    init {
        loadNews()
    }

    fun onIntent(intent: HomeIntent) {
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

            HomeIntent.SelectAllCategory ->{
                selectAllCategory()
            }
        }
    }

    private fun selectAllCategory() {
        currentPage=1
        totalAvailableResults= Int.MAX_VALUE

        _uiState.update {
            it.copy(
                selectedCategory = null,
                news = emptyList(),
                hasMoreData = true,
                error = null
            )
        }
        loadNews(
            category = null,
            isLoadingMore = false
        )
    }

    private fun loadMore() {

        val state = _uiState.value


        if (state.isLoading) return

        if (state.isLoadingMore) return

        if (!state.hasMoreData) return

        val nextStartIndex = (currentPage - 1) * PAGE_SIZE + 1

        val nextEndIndex = nextStartIndex + PAGE_SIZE - 1

        if (nextEndIndex > DEVELOPER_RESULT_LIMIT) {
            _uiState.update {
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

    private fun selectCategory(
        category: String
    ) {

        currentPage = 1

        totalAvailableResults = Int.MAX_VALUE

        _uiState.update {
            it.copy(
                selectedCategory = category,
                news = emptyList(),
                hasMoreData = true,
                error = null
            )
        }

        loadNews(
            category = category,
            isLoadingMore = false
        )
    }

    private fun retry() {

        if (_uiState.value.news.isEmpty()) {

            currentPage = 1

            totalAvailableResults = Int.MAX_VALUE

            loadNews(
                category = _uiState.value.selectedCategory,
                isLoadingMore = false
            )

        } else {

            loadMore()
        }
    }

    private fun loadNews(
        category: String? = null,
        isLoadingMore: Boolean = false
    ) {

        viewModelScope.launch {

            if (isLoadingMore) {

                _uiState.update {
                    it.copy(
                        isLoadingMore = true,
                        error = null
                    )
                }

            } else {

                _uiState.update {
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }
            }

            try {

                val result = repository.getTopHeadlines(
                    category = category,
                    page = currentPage,
                    pageSize = PAGE_SIZE
                )
                println("PAGINATION -> API page = $currentPage")
                println("PAGINATION -> category = $category")
                println("PAGINATION -> result.news.size = ${result.news.size}")
                println("PAGINATION -> totalResult = ${result.totalResult}")
                totalAvailableResults = minOf(
                    result.totalResult,
                    DEVELOPER_RESULT_LIMIT
                )

                _uiState.update { state ->

                    val updatedNews = if (isLoadingMore) {
                            (state.news + result.news)
                                .distinctBy { it.newsUrl }

                        } else {

                            result.news
                        }

                    val nextPage = currentPage + 1

                    val nextStartIndex = (nextPage - 1) * PAGE_SIZE + 1

                    val nextEndIndex = nextStartIndex + PAGE_SIZE - 1

                    val hasMoreData = updatedNews.size < totalAvailableResults && result.news.isNotEmpty() && nextEndIndex <= DEVELOPER_RESULT_LIMIT

                    state.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        news = updatedNews,
                        hasMoreData = hasMoreData,
                        error = null
                    )
                }

                currentPage++

            } catch (ex: Exception) {

                _uiState.update {
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
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true,
    val news: List<News> = emptyList(),
    val selectedCategory: String? = null,
    val error: String? = null
)