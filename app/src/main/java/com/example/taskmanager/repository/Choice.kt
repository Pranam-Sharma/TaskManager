package com.example.taskmanager.repository

import com.example.taskmanager.model.ChatMessage

data class Choice(
    val index: Int,
    val message: ChatMessage,
    val finish_reason: String
)