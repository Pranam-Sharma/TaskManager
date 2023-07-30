package com.example.taskmanager.database.newsdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.taskmanager.model.news.Article
import com.example.taskmanager.model.news.NewsResponse

@Dao
interface NewsDao {
    @Query("SELECT * FROM Article ORDER BY id DESC")
    fun getAllNews(): LiveData<List<Article>>

    @Query("SELECT * FROM Article WHERE author LIKE :query OR content LIKE :query OR description LIKE :query OR publishedAt LIKE :query OR source LIKE :query OR title LIKE :query OR url LIKE :query OR urlToImage LIKE :query ORDER BY id DESC")
    fun searchNews(query: String): LiveData<List<Article>>

    @Delete
    suspend fun deleteNews(note: Article)
}