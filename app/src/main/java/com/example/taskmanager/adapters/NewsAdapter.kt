package com.example.taskmanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taskmanager.R
import com.example.taskmanager.WebViewFragment
import com.example.taskmanager.databinding.ItemNewsBinding
import com.example.taskmanager.model.news.Article
import com.google.android.material.textview.MaterialTextView
import androidx.fragment.app.FragmentManager

class NewsAdapter(private val context: Context) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var newsList: List<Article> = emptyList()

    // Create the ViewHolder class
   inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentBinding= ItemNewsBinding.bind(itemView)
        val title: MaterialTextView =contentBinding.newsItemTitle
        val image: ImageView=contentBinding.newsImage
        val date: MaterialTextView =contentBinding.newsDate

    }

    // Inflate the layout for each item and return a ViewHolder object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(itemView)
    }

    // Bind data to the views in each item
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = newsList[position]

        holder.title.text = article.title
         holder.date.text=article.publishedAt
        Glide.with(context).load(article.urlToImage).into(holder.image)
       holder.itemView.setOnClickListener{
           val url=article.url
           val webViewFragment=WebViewFragment.newInstance(url)
           val fragmentManager = holder.itemView.context
       }
    }

    // Return the number of items in the list
    override fun getItemCount(): Int {
        return newsList.size
    }

    // Update the news list with new data
    fun setNewsList(newsList: List<Article>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }
}
