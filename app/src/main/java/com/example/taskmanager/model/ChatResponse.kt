package com.example.taskmanager.model

import com.example.taskmanager.repository.Choice

data class ChatResponse(
    val id: String,
    val model: String,
    val choices: List<Choice>,
    val system_fingerprint: String,
)