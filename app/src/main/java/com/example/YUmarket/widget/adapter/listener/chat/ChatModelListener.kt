package com.example.YUmarket.widget.adapter.listener.chat

import com.example.YUmarket.model.chat.ChatModel
import com.example.YUmarket.widget.adapter.listener.AdapterListener

interface ChatModelListener: AdapterListener {
    fun onClickItem(listModel: ChatModel)

}