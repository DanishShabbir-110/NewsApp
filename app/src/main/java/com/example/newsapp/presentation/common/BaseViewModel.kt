package com.example.newsapp.presentation.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<Intent,UiState>(initialState: UiState): ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    protected fun updateState(updateState:(UiState)->UiState){
        _uiState.update {
            updateState(it)
        }
    }

    abstract fun onIntent(intent: Intent)
}