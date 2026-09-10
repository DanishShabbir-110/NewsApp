package com.example.newsapp.presentation.main

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.newsapp.presentation.navigation.Routes

fun EntryProviderScope<NavKey>.mainScreenComposable(backStack: NavBackStack<NavKey>) {
    entry<Routes.Main> {
        MainScreen(
            onNewsClick = { news ->
                backStack.add(Routes.Detail(news))
            })
    }
}