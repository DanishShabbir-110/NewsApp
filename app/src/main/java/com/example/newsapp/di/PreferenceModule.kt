package com.example.newsapp.di

import android.content.Context
import com.example.newsapp.data.local.datastore.SearchPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule= module {
    single {
        androidContext().getSharedPreferences(
            "news_app_preferences",
            Context.MODE_PRIVATE
        )
    }
    single {
        SearchPreferences(get())
    }
}