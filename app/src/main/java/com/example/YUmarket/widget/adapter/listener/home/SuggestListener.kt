package com.example.YUmarket.widget.adapter.listener.home

import com.example.YUmarket.model.homelist.SuggestItemModel
import com.example.YUmarket.widget.adapter.listener.AdapterListener

interface SuggestListener : AdapterListener  {

    fun onClickItem(model:SuggestItemModel)

}