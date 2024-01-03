package com.example.taskmanager.model.`interface`

import com.example.taskmanager.model.ChatResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatApiInterface {
    @POST("v1/chat/completions")
    suspend fun getResponse(
        @Header("Authorization") apiKey: String,
        @Body requestBody: RequestBody
    ): Response<ChatResponse>

    companion object {
        fun create(): ChatApiInterface {
            return Retrofit.Builder()
                .baseUrl("https://api.openai.com/") // Replace with your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ChatApiInterface::class.java)
        }
    }
}
