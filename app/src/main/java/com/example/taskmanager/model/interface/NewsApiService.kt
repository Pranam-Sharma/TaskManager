package com.example.taskmanager.model.`interface`


import com.example.taskmanager.model.news.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getTopHeadlines(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String="8abc91953059403d8650f286bad88263"
    ): Response<NewsResponse>

}