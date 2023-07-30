package com.example.taskmanager.model.news

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)