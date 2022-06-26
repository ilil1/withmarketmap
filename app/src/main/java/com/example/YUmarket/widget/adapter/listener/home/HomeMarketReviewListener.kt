package com.example.YUmarket.widget.adapter.listener.home

import com.example.YUmarket.model.homelist.HomeReviewModel
import com.example.YUmarket.widget.adapter.listener.AdapterListener

interface HomeMarketReviewListener : AdapterListener {
    fun onClickItem(model: HomeReviewModel)
}