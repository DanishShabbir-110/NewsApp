package com.example.newsapp.presentation.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.BottomNavItem
import com.example.newsapp.presentation.components.NewsBottomBar
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.presentation.home.HomeViewModel
import com.example.newsapp.presentation.savenews.SaveNewsScreen
import com.example.newsapp.presentation.savenews.SaveNewsViewModel
import com.example.newsapp.presentation.search.SearchScreen
import com.example.newsapp.presentation.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(onNewsClick: (News) -> Unit) {
    var selectedItem by rememberSaveable { mutableStateOf(BottomNavItem.Home) }

    val stateHolder = rememberSaveableStateHolder()

    //View Models and their states
    val homeViewModel: HomeViewModel = koinViewModel()
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    val searchViewModel: SearchViewModel = koinViewModel()
    val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()

    val saveNewsViewModel: SaveNewsViewModel = koinViewModel()
    val saveNewsUiState by saveNewsViewModel.uiState.collectAsStateWithLifecycle()


    BackHandler(enabled = selectedItem != BottomNavItem.Home) {
        selectedItem = BottomNavItem.Home
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NewsBottomBar(
                selectedItem = selectedItem,
                onItemClick = { item ->
                    if (item != selectedItem) {
                        selectedItem = item
                    }
                }
            )
        }
    ) { innerPadding ->
        stateHolder.SaveableStateProvider(
            key = selectedItem
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedItem) {
                    BottomNavItem.Home -> {
                        HomeScreen(
                            uiState = homeUiState,
                            onIntent = homeViewModel::onIntent,
                            onNewsClick = onNewsClick
                        )
                    }

                    BottomNavItem.Search -> {
                        SearchScreen(
                            uiState = searchUiState,
                            onIntent = searchViewModel::onIntent,
                            onNewsClick = onNewsClick
                        )
                    }

                    BottomNavItem.SaveNews -> {
                        SaveNewsScreen(
                            uiState = saveNewsUiState,
                            onIntent = saveNewsViewModel::onIntent,
                            onNewsClick = onNewsClick
                        )
                    }
                }
            }
        }
    }
}