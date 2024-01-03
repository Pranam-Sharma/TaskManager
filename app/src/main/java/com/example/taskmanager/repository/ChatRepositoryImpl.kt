package com.example.taskmanager.repository

import com.example.taskmanager.model.ChatRequest
import com.example.taskmanager.model.ChatResponse
import com.example.taskmanager.model.`interface`.ChatApiInterface
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

import retrofit2.Response

class ChatRepositoryImpl(private val chatApiInterface: ChatApiInterface) : ChatRepository {
    private val API_KEY = "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"

    override suspend fun fetchGPTResponse(chatRequest: ChatRequest): Response<ChatResponse> {
        val requestBody = Gson().toJson(chatRequest).toRequestBody("application/json".toMediaType())
        return chatApiInterface.getResponse(API_KEY,requestBody)
    }
}
