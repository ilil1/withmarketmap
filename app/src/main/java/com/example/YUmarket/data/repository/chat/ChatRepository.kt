package com.example.YUmarket.data.repository.chat

import com.example.YUmarket.model.chat.ChatModel


interface ChatRepository {
    fun findChatList() : List<ChatModel>
}