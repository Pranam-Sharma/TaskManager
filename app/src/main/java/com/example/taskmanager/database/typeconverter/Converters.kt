package com.example.taskmanager.database.typeconverter

import androidx.room.TypeConverter
import com.example.taskmanager.model.news.Article
import com.example.taskmanager.model.news.Source
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromArticleList(articleList: List<Article>): String {
        return gson.toJson(articleList)
    }

    @TypeConverter
    fun toArticleList(articleListString: String): List<Article> {
        val listType = object : TypeToken<List<Article>>() {}.type
        return gson.fromJson(articleListString, listType)
    }

    @TypeConverter
    fun fromSource(source: Source): String {
        return gson.toJson(source)
    }

    @TypeConverter
    fun toSource(sourceString: String): Source {
        return gson.fromJson(sourceString, Source::class.java)
    }
}