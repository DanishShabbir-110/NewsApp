package com.example.newsapp.di

import com.example.newsapp.data.repository.NewsRepositoryImpl
import com.example.newsapp.domain.repository.NewsRepository
import org.koin.dsl.module

val repositoryModule= module {
        single<NewsRepository> {
            NewsRepositoryImpl(apiService = get(), newsDao = get())
        }
}