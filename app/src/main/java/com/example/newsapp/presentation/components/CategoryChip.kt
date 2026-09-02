package com.example.newsapp.presentation.components

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CategoryChip(
    title:String,
    isSelected:Boolean,
    onClick:()->Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {Text(text = title)}
    )
}