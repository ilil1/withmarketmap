package com.example.YUmarket.data.entity.home

import android.os.Parcelable
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.model.CellType
import kotlinx.android.parcel.Parcelize


@Parcelize
class TownMarketEntity(
    val id: Long,
    val marketName: String,
    val isMarketOpen: Boolean,
    val locationLatLngEntity: LocationLatLngEntity,
    val marketImageUrl: String,
    val distance: Float,
    val type: CellType = CellType.HOME_TOWN_MARKET_CELL
) : Parcelable