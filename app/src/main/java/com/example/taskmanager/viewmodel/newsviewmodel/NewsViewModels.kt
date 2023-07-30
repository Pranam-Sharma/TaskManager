package com.example.taskmanager.viewmodel.newsviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.model.news.Article
import com.example.taskmanager.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModels (private val repository: NewsRepository) : ViewModel(){

    private val _newsLiveData = MutableLiveData<List<Article>>()
    val newsLiveData: LiveData<List<Article>> = _newsLiveData




    fun fetchTopHeadlines(country:String, category: String) =
        viewModelScope.launch {
            val response = repository.getTopHeadlines(country, category)
            if (response.isSuccessful) {
                val articles = response.body()?.articles
                _newsLiveData.postValue(articles!!)
                repository.getNews()
            } else {
                _newsLiveData.value = emptyList()
            }
        }


    fun getAllNews(): LiveData<List<Article>> = repository.getNews()

    fun searchNews(query: String): LiveData<List<Article>> {
        return repository.searchNews(query)
    }

    fun deleteNews(delete: Article) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNews(delete)
    }

}