package com.example.newsapp.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
) {
    Home(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home
    ),
    Search(
        title = "Search",
        selectedIcon = Icons.Filled.Search,
        unSelectedIcon = Icons.Outlined.Search
    ),
    SaveNews(
        title = "Saved",
        selectedIcon = Icons.Filled.Bookmark,
        unSelectedIcon = Icons.Outlined.BookmarkBorder
    )
}

@Composable
fun NewsBottomBar(
    selectedItem: BottomNavItem,
    onItemClick: (BottomNavItem) -> Unit
) {
    val items = listOf(BottomNavItem.Home, BottomNavItem.Search, BottomNavItem.SaveNews)
    NavigationBar {
        items.forEach { item ->
            val isSelected = selectedItem == item
            NavigationBarItem(
                selected = isSelected,
                onClick = {onItemClick(item)},
                icon = {
                    Icon(
                        imageVector = if(isSelected) item.selectedIcon else item.unSelectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) }
            )
        }
    }
}