package com.example.newsapp.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.newsapp.presentation.components.CategoryChip

@Composable
fun CategorySection(selectedCategory: String?, onCategoryClick: (String) -> Unit) {

    val categories = listOf(
        "All",
        "Business",
        "Technology",
        "Sports",
        "Health",
        "Science",
        "Entertainment"
    )

    LazyRow(
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(
            items = categories,
            key = { it }
        ) { category ->

            CategoryChip(
                title = category,

                isSelected =
                    if (category == "All") {
                        selectedCategory == null
                    } else {
                        selectedCategory == category.lowercase()
                    },

                onClick = { onCategoryClick(category) }
            )
        }
    }
}