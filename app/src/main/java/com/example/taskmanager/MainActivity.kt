package com.example.taskmanager

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.taskmanager.database.NoteDatabase
import com.example.taskmanager.database.newsdb.NewsDatabase
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.repository.NewsRepository
import com.example.taskmanager.repository.NoteRepository
import com.example.taskmanager.viewmodel.NoteViewModel
import com.example.taskmanager.viewmodel.NoteViewModelFactory
import com.example.taskmanager.viewmodel.newsviewmodel.NewsViewModelFactory
import com.example.taskmanager.viewmodel.newsviewmodel.NewsViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var navController: NavController
        lateinit var noteActivityViewModel: NoteViewModel
        lateinit var newsViewModels:NewsViewModels

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)


        try {
            //NoteViewModel initialization
            setContentView(binding.root)
            val noteRepository=NoteRepository(NoteDatabase(this) as NoteDatabase)
            val noteViewModelProviderfactory=NoteViewModelFactory(noteRepository)
            noteActivityViewModel=ViewModelProvider(this,noteViewModelProviderfactory)[NoteViewModel::class.java]

        }catch (e:Exception){
            Log.d("TAG","Error")
        }

//        try {
//            //NewsViewModel initialization
//            val newsRepository=NewsRepository(NewsDatabase(this))
//            val newsViewModelProviderfactory= NewsViewModelFactory(newsRepository)
//            newsViewModels= ViewModelProvider(this,newsViewModelProviderfactory)[NewsViewModels::class.java]
//        }catch (e:Exception){
//            Log.d("TAG","Error")
//        }


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
        val iconColorStates = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ), intArrayOf(
                Color.parseColor("#123456"),
                Color.parseColor("#654321")
            )
        )

        bottomNavigationView.itemIconTintList = iconColorStates
        bottomNavigationView.itemTextColor = iconColorStates
    }
}