package com.example.newsapp.di

import androidx.room.Room
import com.example.newsapp.data.local.database.NewsDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule=module{
    single{
        Room.databaseBuilder(
            androidContext(),
            NewsDatabase::class.java,
            "app_database"
        ).build()
    }

    single {
        get<NewsDatabase>().getNewsDao()
    }

}