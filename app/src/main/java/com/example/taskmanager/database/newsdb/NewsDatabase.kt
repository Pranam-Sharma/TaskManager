package com.example.taskmanager.database.newsdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskmanager.database.typeconverter.Converters
import com.example.taskmanager.model.news.Article

    @Database(entities = [Article::class], version = 1, exportSchema = false)
    @TypeConverters(Converters::class)
    abstract class NewsDatabase : RoomDatabase() {
        abstract fun geNewsDao(): NewsDao

        companion object {
            @Volatile
            private var INSTANCE: NewsDatabase? = null
            private val LOCK=Any()

            operator fun invoke(context: Context)= INSTANCE?: synchronized(LOCK){
                INSTANCE ?: createDatabase(context).also {
                    INSTANCE=it
                }
            }

            fun createDatabase(context: Context): NewsDatabase = Room.databaseBuilder(
                context.applicationContext,
                NewsDatabase::class.java,
                "news_database"
            ).build()
        }
    }
