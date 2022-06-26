package com.example.YUmarket.data.repository.map

import com.example.YUmarket.model.map.MapMarketModel

interface MapRepository {
    fun getMarkets(): List<MapMarketModel>
}