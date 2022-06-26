package com.example.YUmarket.data.repository.home

import com.example.YUmarket.model.homelist.HomeReviewModel

interface HomeReviewRepository {
    fun findChatList() : List<HomeReviewModel>
}