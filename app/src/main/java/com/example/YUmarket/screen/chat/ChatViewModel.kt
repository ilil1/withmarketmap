package com.example.YUmarket.screen.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.data.repository.chat.ChatRepository
import com.example.YUmarket.model.chat.ChatModel
import com.example.YUmarket.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository
):BaseViewModel() {
    private val _chatListData = MutableLiveData<List<ChatModel>>()
    val chatListData : LiveData<List<ChatModel>> = _chatListData


    override fun fetchData(): Job = viewModelScope.launch {
        _chatListData.value = chatRepository.findChatList()
    }
}