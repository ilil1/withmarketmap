package com.example.YUmarket.data.repository.store

import com.example.YUmarket.model.homelist.SuggestItemModel
import com.example.YUmarket.model.store.StoreItemModel

interface StoreRepository {

    fun findStore() : List<StoreItemModel>



}