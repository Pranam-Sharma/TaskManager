package com.example.taskmanager

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.example.taskmanager.databinding.FragmentWebViewBinding


class WebViewFragment : Fragment(R.layout.fragment_web_view) {

   private lateinit var binding:FragmentWebViewBinding
   private lateinit var webView:WebView

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWebViewBinding.bind(view)
        webView=binding.tvWebView
        val url= arguments?.getString(KEY_URL)


    }
    companion object{
        private const val KEY_URL="url"
        fun newInstance(url:String):WebViewFragment{
            val fragment = WebViewFragment()
            val bundle=Bundle()
            bundle.putString(KEY_URL,url)
            fragment.arguments=bundle
            return fragment
        }
    }
}