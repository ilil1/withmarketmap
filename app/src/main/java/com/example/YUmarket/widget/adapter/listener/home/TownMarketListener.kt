package com.example.YUmarket.widget.adapter.listener.home

import com.example.YUmarket.model.homelist.TownMarketModel
import com.example.YUmarket.widget.adapter.listener.AdapterListener

interface TownMarketListener : AdapterListener {

    fun onClickItem(model: TownMarketModel)

}