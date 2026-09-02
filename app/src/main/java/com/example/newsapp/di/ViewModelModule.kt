package com.example.newsapp.di

import com.example.newsapp.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.viewmodel.scope.viewModelScope

val viewModelModule= module {
    viewModel{
        HomeViewModel(get())
    }
}