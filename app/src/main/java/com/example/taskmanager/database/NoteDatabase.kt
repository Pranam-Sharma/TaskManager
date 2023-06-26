package com.example.taskmanager.database

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskmanager.model.Note

// NoteDatabase.kt
@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null
        private val LOCK=Any()

        operator fun invoke(context: Context)= INSTANCE?: synchronized(LOCK){
            INSTANCE ?: createDatabase(context).also {
                INSTANCE=it
            }
        }

        fun createDatabase(context: Context): NoteDatabase= Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                ).build()
            }
        }
