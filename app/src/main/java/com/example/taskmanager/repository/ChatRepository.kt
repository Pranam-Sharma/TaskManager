package com.example.taskmanager.repository

import com.example.taskmanager.model.ChatRequest
import com.example.taskmanager.model.ChatResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface ChatRepository {
    suspend fun fetchGPTResponse( chatRequest: ChatRequest): Response<ChatResponse>
}
