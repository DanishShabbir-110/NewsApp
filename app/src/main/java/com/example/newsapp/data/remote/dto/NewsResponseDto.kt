package com.example.newsapp.data.remote.dto

data class NewsResponseDto(
    val status:String,
    val totalResults:Int,
    val articles:List<NewsDto>
)
