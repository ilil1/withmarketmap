package com.example.YUmarket.widget.adapter.listener.like

import com.example.YUmarket.model.Model
import com.example.YUmarket.widget.adapter.listener.AdapterListener

interface LikeListener : AdapterListener {
    fun onClick(model: Model)
    fun onDeleteClick(model: Model)
}