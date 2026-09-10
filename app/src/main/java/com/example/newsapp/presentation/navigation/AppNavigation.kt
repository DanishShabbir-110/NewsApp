package com.example.newsapp.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.newsapp.presentation.main.mainScreenComposable
import com.example.newsapp.presentation.newsdetail.newsDetailScreenComposable
import com.example.newsapp.presentation.splash.splashScreenComposable

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(Routes.Splash)
    val entryProvider = entryProvider {
        splashScreenComposable(backStack)
        mainScreenComposable(backStack)
        newsDetailScreenComposable(backStack)
    }
    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = {
            backStack.removeLastOrNull()
        }
    )
}