package com.example.taskmanager.viewmodel.newsviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.repository.NewsRepository

class NewsViewModelFactory(private val repository: NewsRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModels::class.java)) {
            return NewsViewModels(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}