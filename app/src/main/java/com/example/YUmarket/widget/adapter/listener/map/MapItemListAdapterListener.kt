package com.example.YUmarket.widget.adapter.listener.map

import com.example.YUmarket.model.map.MapItemModel
import com.example.YUmarket.widget.adapter.listener.AdapterListener

interface MapItemListAdapterListener : AdapterListener {
    fun onClickItem(mapItemModel: MapItemModel)
}