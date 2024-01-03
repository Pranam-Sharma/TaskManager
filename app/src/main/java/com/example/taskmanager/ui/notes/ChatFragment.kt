package com.example.taskmanager.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.databinding.FragmentChatBinding
import com.example.taskmanager.model.`interface`.ChatApiInterface
import com.example.taskmanager.repository.ChatRepositoryImpl
import com.example.taskmanager.viewmodel.ChatViewModel
import com.example.taskmanager.viewmodel.ChatViewModelFactory
import kotlinx.coroutines.launch

class ChatFragment:Fragment() {

    private lateinit var binding:FragmentChatBinding
    // Use a custom ViewModelFactory to provide the ChatApiInterface instance
    private val chatViewModel by viewModels<ChatViewModel>{
        ChatViewModelFactory(ChatRepositoryImpl(ChatApiInterface.create()))
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener {
           val question=binding.tvQuestion.text.toString().trim()
            binding.questionView.visibility=View.VISIBLE
            binding.questionView.clearFocus()
            binding.question.text=question
            binding.answerView.visibility=View.VISIBLE
            viewLifecycleOwner.lifecycleScope.launch {
                chatViewModel.fetchGPTResponse(question)
            }

            chatViewModel.gptResponse.observe(viewLifecycleOwner, Observer {
                if (!it.isNullOrEmpty()){

                    binding.answer.text=it.toString()
                }
            })
        }
    }
}