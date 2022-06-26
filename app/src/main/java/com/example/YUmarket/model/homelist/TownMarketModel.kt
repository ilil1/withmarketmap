package com.example.YUmarket.model.homelist

import com.example.YUmarket.data.entity.home.TownMarketEntity
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.Model

data class TownMarketModel(
    override val id: Long,
    val marketName: String,
    val isMarketOpen: Boolean,
    val locationLatLngEntity: LocationLatLngEntity,
    val marketImageUrl: String,
    val distance: Float, // 22.01.18 거리 추가 by 정남진
    override val type: CellType = CellType.HOME_TOWN_MARKET_CELL
) : Model(id, type) {

    fun toEntity() = TownMarketEntity(
        id,
        marketName,
        isMarketOpen,
        locationLatLngEntity,
        marketImageUrl,
        distance,
        type
    )
}