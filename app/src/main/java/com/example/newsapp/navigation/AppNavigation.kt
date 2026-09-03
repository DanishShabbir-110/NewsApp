package com.example.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.presentation.newsdetail.NewsDetailScreen
import com.example.newsapp.presentation.search.SearchScreen

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack (Routes.Home)
    NavDisplay(
        backStack=backStack,
        onBack = {backStack.removeLastOrNull()},
        entryProvider=entryProvider {
            entry<Routes.Home> {
                HomeScreen(
                    onNewsClick = {news->
                        backStack.add(Routes.Detail(news = news))
                    }
                )
            }
            entry<Routes.Detail> {route ->
                NewsDetailScreen(
                    news = route.news,
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }
            entry<Routes.Search>{
                SearchScreen()
            }
        }
    )

}