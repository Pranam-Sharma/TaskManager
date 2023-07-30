package com.example.taskmanager.utils

import com.example.taskmanager.model.`interface`.NewsApiService
import com.example.taskmanager.model.news.NewsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitNewsClient {
    private const val BASE_URL = "https://newsapi.org/"

    fun create(): NewsApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(NewsApiService::class.java)
    }
}