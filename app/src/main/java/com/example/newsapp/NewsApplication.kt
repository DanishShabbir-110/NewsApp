package com.example.newsapp

import android.app.Application
import com.example.newsapp.di.databaseModule
import com.example.newsapp.di.networkModule
import com.example.newsapp.di.preferencesModule
import com.example.newsapp.di.repositoryModule
import com.example.newsapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NewsApplication)

            modules(
                networkModule, repositoryModule,
                viewModelModule, preferencesModule,
                databaseModule
            )
        }
    }
}