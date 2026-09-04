package com.example.newsapp.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.newsapp.presentation.components.BottomNavItem
import com.example.newsapp.presentation.components.NewsBottomBar
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.presentation.newsdetail.NewsDetailScreen
import com.example.newsapp.presentation.savenews.SaveNewsScreen
import com.example.newsapp.presentation.search.SearchScreen

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(Routes.Home)

    val currentRoute = backStack.lastOrNull()

    val selectedBottomItem = when (currentRoute) {
        Routes.Home -> BottomNavItem.Home
        Routes.Search -> BottomNavItem.Search
        Routes.SaveNews -> BottomNavItem.SaveNews
        else -> null
    }

    val showBottomBar =
        currentRoute is Routes.Home || currentRoute is Routes.Search || currentRoute is Routes.SaveNews

    Scaffold(
        contentWindowInsets = WindowInsets(0,0,0,0),
        bottomBar = {
            if (showBottomBar && selectedBottomItem != null) {
                NewsBottomBar(
                    selectedItem = selectedBottomItem,
                    onItemClick = { item ->
                        when (item) {
                            BottomNavItem.Home -> {
                                navigationTopLevel(
                                    backStack = backStack,
                                    route = Routes.Home
                                )
                            }

                            BottomNavItem.Search -> {
                                navigationTopLevel(
                                    backStack = backStack,
                                    route = Routes.Search
                                )
                            }

                            BottomNavItem.SaveNews -> {
                                navigationTopLevel(
                                    backStack = backStack,
                                    route = Routes.SaveNews
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(innerPadding),
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = entryProvider {
                entry<Routes.Home> {
                    HomeScreen(
                        onNewsClick = { news ->
                            backStack.add(Routes.Detail(news = news))
                        }
                    )
                }
                entry<Routes.Detail> { route ->
                    NewsDetailScreen(
                        news = route.news,
                        onBackClick = { backStack.removeLastOrNull() }
                    )
                }
                entry<Routes.Search> {
                    SearchScreen(
                        onNewsClick = { news ->
                            backStack.add(Routes.Detail(news = news))
                        }
                    )
                }

                entry<Routes.SaveNews> {
                    SaveNewsScreen(
                        onNewsClick = { news ->
                            backStack.add(Routes.Detail(news = news))
                        }
                    )
                }
            }
        )
    }
}

private fun navigationTopLevel(
    backStack: NavBackStack<NavKey>,
    route: Routes
) {
    if (backStack.lastOrNull() == route) return
    backStack.clear()
    backStack.add(route)
}