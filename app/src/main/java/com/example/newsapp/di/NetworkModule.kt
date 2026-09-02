package com.example.newsapp.di

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.remote.api.ApiKeyInterceptor
import com.example.newsapp.data.remote.api.NewsApiService
import com.example.newsapp.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        ApiKeyInterceptor(apiKey = BuildConfig.NEWS_API_KEY)
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder().addInterceptor(get<ApiKeyInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>()).build()
    }

    single {
        Retrofit.Builder().baseUrl(BASE_URL).client(get())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    single {
        get<Retrofit>().create(NewsApiService::class.java)
    }
}