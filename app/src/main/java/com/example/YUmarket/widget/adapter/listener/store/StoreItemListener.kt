package com.example.YUmarket.widget.adapter.listener.store

import com.example.YUmarket.model.chat.ChatModel
import com.example.YUmarket.model.store.StoreItemModel
import com.example.YUmarket.widget.adapter.listener.AdapterListener

interface StoreItemListener: AdapterListener {
    fun onClickItem(listModel: StoreItemModel)
}