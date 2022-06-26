package com.example.YUmarket.model.map

import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.Model
import com.naver.maps.geometry.LatLng

data class MapMarketModel(
    override val id: Long,
    val name : String,
    val location : LatLng,
    val distance : Float,
    val items : ArrayList<MapItemModel>,
    val branch : String = "",
    override val type: CellType = CellType.MAP_ITEM_CELL // TODO 수정
): Model(id, type)