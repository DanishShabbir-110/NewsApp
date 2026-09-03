package com.example.newsapp.presentation.newsdetail

import androidx.lifecycle.ViewModel
import com.example.newsapp.domain.model.News
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewsDetailViewModel: ViewModel() {
    private var _uiState= MutableStateFlow(NewsDetailUiState())
    val uiState=_uiState.asStateFlow()

    fun onIntent(intent:NewsDetailIntent){
        when(intent){
            is NewsDetailIntent.SetNews ->{
                setNews(intent.news)
            }
        }
    }

    private fun setNews(news: News) {
        _uiState.update {
            it.copy(news=news)
        }
    }

}

data class NewsDetailUiState(
    val news: News?=null,
    val isBookMarked: Boolean=false
)