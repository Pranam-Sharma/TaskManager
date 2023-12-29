package com.example.taskmanager.ui.notes

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.WebViewFragment
import com.example.taskmanager.adapters.NewsAdapter
import com.example.taskmanager.database.newsdb.NewsDatabase
import com.example.taskmanager.databinding.FragmentHomeBinding
import com.example.taskmanager.model.`interface`.NewsApiService
import com.example.taskmanager.repository.NewsRepository
import com.example.taskmanager.utils.RetrofitNewsClient
import com.example.taskmanager.viewmodel.newsviewmodel.NewsViewModelFactory
import com.example.taskmanager.viewmodel.newsviewmodel.NewsViewModels


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsViewmodel: NewsViewModels
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var fragment: Fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initViewModel()
        initialiseView()
        fragment = WebViewFragment()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            requireActivity().finishAndRemoveTask()
        }

        newsViewmodel.fetchTopHeadlines("everything")
        binding.tvProgress.visibility = View.VISIBLE
        newsViewmodel.newsLiveData.observe(viewLifecycleOwner, Observer { Articles ->
            if (Articles.isNullOrEmpty()) {
                binding.errLayout.visibility = View.VISIBLE
            } else {
                binding.tvProgress.visibility = View.GONE
                binding.errLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                newsAdapter.setNewsList(Articles)
            }
        })

        binding.searchNews.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (toString().isNotEmpty()) {
                    val text = s.toString()
                    val query = "%$text%"
                    if (query.isNotEmpty()) {
                        newsViewmodel.fetchTopHeadlines(query)
                        newsViewmodel.newsLiveData.observe(
                            viewLifecycleOwner,
                            Observer { Articles ->
                                if (Articles.isNullOrEmpty()) {
                                    binding.errLayout.visibility = View.VISIBLE
                                } else {
                                    binding.errLayout.visibility = View.GONE
                                    binding.recyclerView.visibility = View.VISIBLE
                                    newsAdapter.setNewsList(Articles)
                                    newsAdapter.onItemClick = { article ->

                                    }
                                }
                            })
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //Do nothing

//              if (toString().isNotEmpty()) {
//                  val text = s.toString()
//                  val query = "%$text%"
//                  if (query.isNotEmpty()) {
//
//                      newsViewmodel.newsLiveData.observe(viewLifecycleOwner, Observer { Articles ->
//                          newsViewmodel.fetchTopHeadlines(query)
//                          newsAdapter.setNewsList(Articles)
//                          Log.d("Articles",Articles.toString())
//                      })
//                  }
//
//              }
            }

        })

    }

    private fun initialiseView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(requireContext())
        recyclerView.adapter = newsAdapter

    }

    private fun initViewModel() {
        val apiServices: NewsApiService = RetrofitNewsClient.create()
        val newsRepository = NewsRepository(NewsDatabase(requireContext()), apiServices)
        val newsViewModelProviderfactory = NewsViewModelFactory(newsRepository)
        newsViewmodel =
            ViewModelProvider(this, newsViewModelProviderfactory)[NewsViewModels::class.java]
    }

}
