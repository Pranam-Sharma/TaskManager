package com.example.taskmanager.model

data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>
)