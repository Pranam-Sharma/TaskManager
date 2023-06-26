package com.example.taskmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.model.Note
import com.example.taskmanager.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// NoteViewModel.kt
class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    fun saveNote(newNote:Note)=viewModelScope.launch(Dispatchers.IO) {
        repository.addNotes(newNote)
    }

    fun updateNote(existingNote:Note)=viewModelScope.launch(Dispatchers.IO) {
        repository.updateNotes(existingNote)
    }

    fun deleteNote(existingNote:Note)=viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNotes(existingNote)
    }

    fun searchNote(query: String) :LiveData<List<Note>>{
        return repository.searchNote(query)
    }

    fun getAllNotes():LiveData<List<Note>> = repository.getNotes()

}
