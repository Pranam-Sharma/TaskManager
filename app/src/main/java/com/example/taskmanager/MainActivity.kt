package com.example.taskmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.taskmanager.database.NoteDatabase
import com.example.taskmanager.databinding.ActivityMainBinding

import com.example.taskmanager.repository.NoteRepository
import com.example.taskmanager.viewmodel.NoteViewModel
import com.example.taskmanager.viewmodel.NoteViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var navController: NavController
        lateinit var noteActivityViewModel: NoteViewModel

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)


        try {
            setContentView(binding.root)
            val noteRepository=NoteRepository(NoteDatabase(this) as NoteDatabase)
            val noteViewModelProviderfactory=NoteViewModelFactory(noteRepository)
            noteActivityViewModel=ViewModelProvider(this,noteViewModelProviderfactory)[NoteViewModel::class.java]
        }catch (e:Exception){
            Log.d("TAG","Error")
        }


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
    }
}