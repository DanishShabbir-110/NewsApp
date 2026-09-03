package com.example.newsapp.di

import com.example.newsapp.presentation.home.HomeViewModel
import com.example.newsapp.presentation.newsdetail.NewsDetailViewModel
import com.example.newsapp.presentation.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel(get())
    }

    viewModel {
        NewsDetailViewModel(get())
    }

    viewModel {
        SearchViewModel(repository = get(), searchPreferences = get())
    }
}