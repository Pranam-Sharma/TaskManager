package com.example.taskmanager.repository

import android.content.Context
import com.example.taskmanager.database.newsdb.NewsDatabase
import com.example.taskmanager.model.`interface`.NewsApiService
import com.example.taskmanager.model.news.Article
import com.example.taskmanager.model.news.NewsResponse
import retrofit2.Response


class NewsRepository(private var newsDb:NewsDatabase, private val apiService: NewsApiService) {

    suspend fun getTopHeadlines(country: String, category: String): Response<NewsResponse> {

           return apiService.getTopHeadlines(country, category)

        }

    fun getNews()=newsDb.geNewsDao().getAllNews()
    fun searchNews(query:String)=newsDb.geNewsDao().searchNews(query)
    suspend fun deleteNews(article: Article) =newsDb.geNewsDao().deleteNews(article)
}