package com.example.taskmanager.repository

import com.example.taskmanager.database.NoteDao
import com.example.taskmanager.database.NoteDatabase
import com.example.taskmanager.model.Note

// NoteRepository.kt
class NoteRepository(private val db:NoteDatabase) {
    fun getNotes() =db.getNoteDao().getAllNotes()

    fun searchNote(query:String)=db.getNoteDao().searchNote(query)

   suspend fun addNotes(note: Note) =db.getNoteDao().addNote(note)

    suspend fun updateNotes(note: Note) =db.getNoteDao().updateNote(note)

    suspend fun deleteNotes(note: Note) =db.getNoteDao().deleteNote(note)
}
