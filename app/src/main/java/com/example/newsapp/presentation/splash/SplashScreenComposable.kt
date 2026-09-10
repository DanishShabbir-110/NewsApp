package com.example.newsapp.presentation.splash

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.newsapp.presentation.navigation.Routes

fun EntryProviderScope<NavKey>.splashScreenComposable(backStack: NavBackStack<NavKey>) {
    entry<Routes.Splash> {
        SplashScreen(
            onSplashFinished = {
                backStack.clear()
                backStack.add(Routes.Main)
            }
        )
    }
}