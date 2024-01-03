package com.example.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmanager.model.ChatMessage
import com.example.taskmanager.model.ChatRequest
import com.example.taskmanager.repository.ChatRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay


class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {
    private val _gptResponse = MutableLiveData<String>()
    val gptResponse: LiveData<String>
        get() = _gptResponse

    suspend fun fetchGPTResponse(userMessage: String) {
        val chatRequest = ChatRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(
                ChatMessage("system", "You are a helpful assistant."),
                ChatMessage("user", userMessage)
            )
        )

        try {
            coroutineScope {
                delay(3000L)
                val response = chatRepository.fetchGPTResponse(chatRequest)
                if (response.isSuccessful) {
                    val assistantMessage = response.body()?.choices?.get(0)?.message?.content ?: "Empty response"
                    _gptResponse.value = assistantMessage
                } else {
                    _gptResponse.value = "Error: ${response.code()}"
                }
            }

        } catch (e: Exception) {
            _gptResponse.value = "Error: ${e.message}"
        }
    }
}