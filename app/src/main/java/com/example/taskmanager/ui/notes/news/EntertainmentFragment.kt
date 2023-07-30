package com.example.taskmanager.ui.notes.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.adapters.NewsAdapter
import com.example.taskmanager.database.newsdb.NewsDatabase
import com.example.taskmanager.databinding.FragmentEntertainmentBinding
import com.example.taskmanager.databinding.FragmentSportBinding
import com.example.taskmanager.model.`interface`.NewsApiService
import com.example.taskmanager.repository.NewsRepository
import com.example.taskmanager.utils.RetrofitNewsClient
import com.example.taskmanager.viewmodel.newsviewmodel.NewsViewModelFactory
import com.example.taskmanager.viewmodel.newsviewmodel.NewsViewModels

class EntertainmentFragment : Fragment(R.layout.fragment_entertainment) {

    private lateinit var binding: FragmentEntertainmentBinding
    private lateinit var newsViewModel: NewsViewModels
    private lateinit var progressBar:ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEntertainmentBinding.bind(view)
        val apiServices: NewsApiService = RetrofitNewsClient.create()
        val newsRepository = NewsRepository(NewsDatabase(requireContext()), apiServices)
        val newsViewModelProviderfactory = NewsViewModelFactory(newsRepository)
        newsViewModel =
            ViewModelProvider(this, newsViewModelProviderfactory)[NewsViewModels::class.java]

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val newsAdapter= NewsAdapter(requireContext())
        recyclerView.adapter=newsAdapter
        newsViewModel.fetchTopHeadlines("us","entertainment")
        progressBar=binding.tvProgress
        progressBar.visibility=View.VISIBLE
        newsViewModel.newsLiveData.observe(viewLifecycleOwner) {
            Log.d("NewsResponseEntertainment",it.toString())
            newsAdapter.setNewsList(it)
            progressBar.visibility=View.GONE
        }

    }
}