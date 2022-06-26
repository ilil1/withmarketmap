package com.example.YUmarket.widget.adapter.listener.home

import com.example.YUmarket.model.homelist.HomeItemModel
import com.example.YUmarket.widget.adapter.listener.AdapterListener

interface HomeListListener : AdapterListener {

    fun onClickItem(Model: HomeItemModel)
}