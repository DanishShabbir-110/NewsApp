package com.example.newsapp.di

import org.koin.dsl.module

val appModule = module {

    includes(networkModule)
    includes(repositoryModule)
    includes(viewModelModule)
    includes(preferencesModule)
    includes(databaseModule)

}